package com.bottari.presentation.view.edit.alarm

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.bottari.di.UseCaseProvider
import com.bottari.domain.model.alarm.Alarm
import com.bottari.domain.usecase.alarm.CreateAlarmUseCase
import com.bottari.domain.usecase.alarm.SaveAlarmUseCase
import com.bottari.logger.BottariLogger
import com.bottari.logger.model.UiEventType
import com.bottari.presentation.common.base.BaseViewModel
import com.bottari.presentation.mapper.AlarmMapper.toDomain
import com.bottari.presentation.model.AlarmTypeUiModel
import com.bottari.presentation.model.AlarmUiModel
import com.bottari.presentation.model.NotificationUiModel
import com.bottari.presentation.model.RepeatDayUiModel
import java.time.LocalDate
import java.time.LocalTime

class AlarmEditViewModel(
    stateHandle: SavedStateHandle,
    private val createAlarmUseCase: CreateAlarmUseCase,
    private val saveAlarmUseCase: SaveAlarmUseCase,
) : BaseViewModel<AlarmUiState, AlarmUiEvent>(
        AlarmUiState(
            alarm = stateHandle[KEY_ALARM] ?: AlarmUiModel.DEFAULT_ALARM_UI_MODEL,
        ),
    ) {
    private val bottariId: Long = stateHandle[KEY_BOTTARI_ID] ?: error(ERROR_REQUIRE_BOTTARI_ID)
    private val bottariTitle: String =
        stateHandle[KEY_BOTTARI_TITLE] ?: error(ERROR_REQUIRE_BOTTARI_TITLE)

    fun updateAlarm() {
        if (isEveryWeekRepeatWithoutSelectedDay()) return

        val alarmDomain = currentState.alarm.toDomain()
        if (alarmDomain.id == null) {
            createAlarm(alarmDomain)
            return
        }
        saveAlarm(alarmDomain)
    }

    fun updateAlarmType(alarmTypeUiModel: AlarmTypeUiModel) {
        updateState { copy(alarm = alarm.copy(type = alarmTypeUiModel)) }
    }

    fun updateAlarmTime(time: LocalTime) {
        updateState { copy(alarm = alarm.copy(time = time)) }
    }

    fun updateAlarmDate(date: LocalDate) {
        if (currentState.alarm.type != AlarmTypeUiModel.NON_REPEAT) return
        updateState { copy(alarm = alarm.copy(date = date)) }
    }

    fun updateDaysOfWeek(dayOfWeek: RepeatDayUiModel) {
        val newRepeatDays =
            currentState.alarm.repeatDays.map {
                if (it.dayOfWeek != dayOfWeek.dayOfWeek) return@map it
                it.copy(isChecked = !it.isChecked)
            }
        val newAlarm = currentState.alarm.copy(repeatDays = newRepeatDays)
        updateState { copy(alarm = newAlarm) }
    }

    private fun isEveryWeekRepeatWithoutSelectedDay(): Boolean =
        currentState.alarm.type == AlarmTypeUiModel.REPEAT &&
            currentState.alarm.repeatDays.none {
                it.isChecked
            }

    private fun createAlarm(alarm: Alarm) {
        updateState { copy(isLoading = true) }
        launch {
            createAlarmUseCase(bottariId, bottariTitle, alarm)
                .onSuccess {
                    BottariLogger.ui(
                        UiEventType.ALARM_CREATE,
                        mapOf("alarm_id" to it, "alarm_info" to alarm.toString()),
                    )
                    emitEvent(AlarmUiEvent.AlarmCreateSuccess(createNotification()))
                }.onFailure {
                    emitEvent(AlarmUiEvent.AlarmCreateFailure)
                }
        }
        updateState { copy(isLoading = false) }
    }

    private fun saveAlarm(alarm: Alarm) {
        updateState { copy(isLoading = true) }
        launch {
            saveAlarmUseCase(bottariId, bottariTitle, alarm)
                .onSuccess {
                    BottariLogger.ui(
                        UiEventType.ALARM_EDIT,
                        mapOf(
                            "alarm_id" to alarm.id!!,
                            "old_alarm_info" to alarm.toString(),
                            "new_alarm_info" to currentState.alarm.toString(),
                        ),
                    )
                    emitEvent(AlarmUiEvent.AlarmSaveSuccess(createNotification()))
                }.onFailure {
                    emitEvent(AlarmUiEvent.AlarmSaveFailure)
                }
        }
        updateState { copy(isLoading = false) }
    }

    private fun createNotification(): NotificationUiModel =
        NotificationUiModel(
            id = bottariId,
            title = bottariTitle,
            alarm = currentState.alarm,
        )

    companion object {
        private const val KEY_BOTTARI_ID = "KEY_BOTTARI_ID"
        private const val KEY_BOTTARI_TITLE = "KEY_BOTTARI_TITLE"
        private const val KEY_ALARM = "KEY_ALARM"
        private const val ERROR_REQUIRE_BOTTARI_ID = "[ERROR] 보따리 ID가 존재하지 않습니다."
        private const val ERROR_REQUIRE_BOTTARI_TITLE = "[ERROR] 보따리 이름이 존재하지 않습니다."

        fun Factory(
            bottariId: Long?,
            bottariTitle: String?,
            alarm: AlarmUiModel?,
        ): ViewModelProvider.Factory =
            viewModelFactory {
                initializer {
                    val stateHandle = createSavedStateHandle()
                    stateHandle[KEY_BOTTARI_ID] = bottariId
                    stateHandle[KEY_BOTTARI_TITLE] = bottariTitle
                    stateHandle[KEY_ALARM] = alarm
                    AlarmEditViewModel(
                        stateHandle = stateHandle,
                        createAlarmUseCase = UseCaseProvider.createAlarmUseCase,
                        saveAlarmUseCase = UseCaseProvider.saveAlarmUseCase,
                    )
                }
            }
    }
}
