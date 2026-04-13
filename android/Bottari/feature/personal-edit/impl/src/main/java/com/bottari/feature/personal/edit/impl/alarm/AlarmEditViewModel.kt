package com.bottari.feature.personal.edit.impl.alarm

import androidx.lifecycle.viewModelScope
import com.bottari.common.util.AlarmScheduler
import com.bottari.common.util.debounce
import com.bottari.core.domain.model.alarm.Alarm
import com.bottari.core.domain.model.notification.Notification
import com.bottari.core.domain.usecase.alarm.FindAlarmUseCase
import com.bottari.core.domain.usecase.alarm.SaveAlarmUseCase
import com.bottari.core.ui.base.BaseViewModel
import com.bottari.core.ui.model.alarm.AlarmTypeUiModel
import com.bottari.core.ui.model.alarm.AlarmUiModel
import com.bottari.core.ui.model.alarm.RepeatDayUiModel
import com.bottari.logger.BottariLogger
import com.bottari.logger.model.UiEventType
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.onCompletion
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

@HiltViewModel(assistedFactory = AlarmEditViewModel.Factory::class)
class AlarmEditViewModel @AssistedInject constructor(
    @Assisted private val bottariId: Long,
    @Assisted private val bottariTitle: String,
    private val findAlarmUseCase: FindAlarmUseCase,
    private val saveAlarmUseCase: SaveAlarmUseCase,
    private val alarmScheduler: AlarmScheduler,
) : BaseViewModel<AlarmUiState, AlarmUiEvent>(AlarmUiState()) {
    private val debouncedAlarmSave: (Unit) -> Unit

    init {
        debouncedAlarmSave = viewModelScope.debounce { saveAlarm() }
        fetchAlarm()
    }

    fun updateAlarmActivate(isActive: Boolean) {
        val alarm = currentState.alarm
        updateState { copy(alarm = alarm.copy(isActive = isActive)) }
        debouncedAlarmSave(Unit)
    }

    fun updateAlarmTime(time: LocalTime) {
        val alarm = currentState.alarm

        val shouldDelayToNextDay =
            alarm.type == AlarmTypeUiModel.NON_REPEAT &&
                LocalDateTime.of(alarm.date, time).isBefore(LocalDateTime.now())

        val updatedAlarm =
            alarm.copy(
                date = if (shouldDelayToNextDay) alarm.date.plusDays(1) else alarm.date,
                time = time,
            )
        updateState { copy(alarm = updatedAlarm) }
        debouncedAlarmSave(Unit)
    }

    fun updateAlarmDate(date: LocalDate) {
        val alarm = currentState.alarm
        val updatedAlarm =
            alarm.copy(
                type = AlarmTypeUiModel.NON_REPEAT,
                date = date,
                repeatDays = RepeatDayUiModel.DEFAULT_WEEK,
            )
        updateState { copy(alarm = updatedAlarm) }
        debouncedAlarmSave(Unit)
    }

    fun updateRepeatDays(repeatDay: RepeatDayUiModel) {
        val alarm = currentState.alarm

        val updatedRepeatDays =
            alarm.repeatDays.map { day ->
                if (day.dayOfWeek != repeatDay.dayOfWeek) return@map day
                day.copy(isChecked = !day.isChecked)
            }
        val hasCheckedDay = updatedRepeatDays.any { day -> day.isChecked }
        val updatedAlarm =
            alarm.copy(
                type = if (hasCheckedDay) AlarmTypeUiModel.REPEAT else AlarmTypeUiModel.NON_REPEAT,
                repeatDays = updatedRepeatDays,
            )

        updateState { copy(alarm = updatedAlarm) }
        debouncedAlarmSave(Unit)
    }

    private fun fetchAlarm() {
        launch {
            updateState { copy(isLoading = true) }
            val alarm =
                findAlarmUseCase(bottariId)
                    .catch {
                        emitEvent(AlarmUiEvent.FetchAlarmFailure)
                    }.onCompletion {
                        updateState { copy(isLoading = false) }
                    }.firstOrNull() ?: return@launch
            updateState { copy(isFetched = true, alarm = AlarmUiModel.fromDomain(alarm)) }
        }
    }

    private fun saveAlarm() {
        if (currentState.isSavable.not()) return
        val alarm = currentState.alarm.toDomain()
        launch {
            saveAlarmUseCase(bottariId, bottariTitle, alarm)
                .onSuccess {
                    BottariLogger.ui(
                        UiEventType.ALARM_EDIT,
                        mapOf(
                            "alarm_id" to alarm.id.toString(),
                            "old_alarm_info" to alarm.toString(),
                            "new_alarm_info" to currentState.alarm.toString(),
                        ),
                    )
                    handleAlarm(alarm)
                }.onFailure {
                    BottariLogger.error(it.stackTraceToString())
                    emitEvent(AlarmUiEvent.SaveAlarmFailure)
                }
        }
    }

    private fun handleAlarm(alarm: Alarm) {
        val notification = createNotification(alarm)
        if (alarm.isActive.not()) {
            alarmScheduler.cancelAlarm(notification)
            return
        }
        alarmScheduler.scheduleAlarm(notification)
    }

    private fun createNotification(alarm: Alarm): Notification =
        Notification(
            bottariId = bottariId,
            bottariTitle = bottariTitle,
            alarm = alarm,
        )

    @AssistedFactory
    interface Factory {
        fun create(
            bottariId: Long,
            bottariTitle: String,
        ): AlarmEditViewModel
    }
}
