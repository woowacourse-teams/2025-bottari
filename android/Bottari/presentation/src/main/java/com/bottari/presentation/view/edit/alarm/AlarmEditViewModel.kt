package com.bottari.presentation.view.edit.alarm

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.bottari.domain.model.alarm.Alarm
import com.bottari.domain.model.notification.Notification
import com.bottari.domain.usecase.alarm.FindAlarmUseCase
import com.bottari.domain.usecase.alarm.SaveAlarmUseCase
import com.bottari.logger.BottariLogger
import com.bottari.logger.model.UiEventType
import com.bottari.presentation.common.base.FlowBaseViewModel
import com.bottari.presentation.model.alarm.AlarmTypeUiModel
import com.bottari.presentation.model.alarm.AlarmUiModel
import com.bottari.presentation.model.alarm.AlarmUiModel.Companion.DEFAULT_ALARM_UI_MODEL
import com.bottari.presentation.model.alarm.RepeatDayUiModel
import com.bottari.presentation.util.AlarmScheduler
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import java.time.LocalDate
import java.time.LocalTime
import javax.inject.Inject

@HiltViewModel
class AlarmEditViewModel @Inject constructor(
    stateHandle: SavedStateHandle,
    private val findAlarmUseCase: FindAlarmUseCase,
    private val saveAlarmUseCase: SaveAlarmUseCase,
    private val alarmScheduler: AlarmScheduler,
) : FlowBaseViewModel<AlarmUiState, AlarmUiEvent>(AlarmUiState()) {
    private val bottariId: Long = stateHandle[KEY_BOTTARI_ID] ?: error(ERROR_REQUIRE_BOTTARI_ID)
    private val bottariTitle: String =
        stateHandle[KEY_BOTTARI_TITLE] ?: error(ERROR_REQUIRE_BOTTARI_TITLE)

    init {
        fetchAlarm()
    }

    fun updateAlarm() {
        val alarm = currentState.alarm?.toDomain() ?: return
        saveAlarm(alarm)
    }

    fun updateAlarmType(alarmTypeUiModel: AlarmTypeUiModel) {
        val alarm = currentState.alarm ?: return
        updateState { copy(alarm = alarm.copy(type = alarmTypeUiModel)) }
    }

    fun updateAlarmTime(time: LocalTime) {
        val alarm = currentState.alarm ?: return
        updateState { copy(alarm = alarm.copy(time = time)) }
    }

    fun updateAlarmDate(date: LocalDate) {
        val alarm = currentState.alarm ?: return
        if (alarm.type != AlarmTypeUiModel.NON_REPEAT) return
        updateState { copy(alarm = alarm.copy(date = date)) }
    }

    fun updateDaysOfWeek(dayOfWeek: RepeatDayUiModel) {
        val alarm = currentState.alarm ?: return
        val newRepeatDays =
            alarm.repeatDays.map {
                if (it.dayOfWeek != dayOfWeek.dayOfWeek) return@map it
                it.copy(isChecked = !it.isChecked)
            }
        val newAlarm = alarm.copy(repeatDays = newRepeatDays)
        updateState { copy(alarm = newAlarm) }
    }

    private fun fetchAlarm() {
        updateState { copy(isLoading = true) }
        findAlarmUseCase(bottariId)
            .onEach { alarm ->
                updateState {
                    copy(
                        isLoading = false,
                        alarm = alarm?.let(AlarmUiModel::fromDomain) ?: DEFAULT_ALARM_UI_MODEL,
                    )
                }
            }.catch {
                updateState { copy(isLoading = false) }
                emitEvent(AlarmUiEvent.FetchAlarmFailure)
            }.launchIn(viewModelScope)
    }

    private fun saveAlarm(alarm: Alarm) {
        updateState { copy(isLoading = true) }
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
                    emitEvent(AlarmUiEvent.SaveAlarmSuccess)
                    alarmScheduler.scheduleAlarm(createNotification(alarm))
                }.onFailure {
                    emitEvent(AlarmUiEvent.SaveAlarmFailure)
                }
        }
        updateState { copy(isLoading = false) }
    }

    private fun createNotification(alarm: Alarm): Notification =
        Notification(
            bottariId = bottariId,
            bottariTitle = bottariTitle,
            alarm = alarm,
        )

    companion object {
        const val KEY_BOTTARI_ID = "KEY_BOTTARI_ID"
        const val KEY_BOTTARI_TITLE = "KEY_BOTTARI_TITLE"

        private const val ERROR_REQUIRE_BOTTARI_ID = "[ERROR] 보따리 ID가 존재하지 않습니다."
        private const val ERROR_REQUIRE_BOTTARI_TITLE = "[ERROR] 보따리 이름이 존재하지 않습니다."
    }
}
