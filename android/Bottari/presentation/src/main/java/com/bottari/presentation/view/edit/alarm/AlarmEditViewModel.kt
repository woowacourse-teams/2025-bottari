package com.bottari.presentation.view.edit.alarm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.bottari.di.UseCaseProvider
import com.bottari.domain.model.alarm.Alarm
import com.bottari.domain.usecase.alarm.CreateAlarmUseCase
import com.bottari.domain.usecase.alarm.SaveAlarmUseCase
import com.bottari.presentation.common.event.SingleLiveEvent
import com.bottari.presentation.common.extension.update
import com.bottari.presentation.mapper.AlarmMapper.toDomain
import com.bottari.presentation.model.AlarmTypeUiModel
import com.bottari.presentation.model.AlarmUiModel
import com.bottari.presentation.model.DayOfWeekUiModel
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalTime

class AlarmEditViewModel(
    stateHandle: SavedStateHandle,
    private val createAlarmUseCase: CreateAlarmUseCase,
    private val saveAlarmUseCase: SaveAlarmUseCase,
) : ViewModel() {
    private val _uiState: MutableLiveData<AlarmUiState> =
        MutableLiveData(
            AlarmUiState(
                alarm = stateHandle[KEY_ALARM] ?: AlarmUiModel.DEFAULT_ALARM_UI_MODEL,
            ),
        )
    val uiState: LiveData<AlarmUiState> get() = _uiState

    private val _uiEvent: SingleLiveEvent<AlarmUiEvent> = SingleLiveEvent()
    val uiEvent: LiveData<AlarmUiEvent> get() = _uiEvent

    private val ssaid: String = stateHandle[KEY_SSAID] ?: error(ERROR_REQUIRE_SSAID)
    private val bottariId: Long = stateHandle[KEY_BOTTARI_ID] ?: error(ERROR_REQUIRE_BOTTARI_ID)

    fun updateAlarm() {
        val currentAlarm = _uiState.value?.alarm ?: return
        if (currentAlarm.type == AlarmTypeUiModel.EVERYWEEK_REPEAT &&
            currentAlarm.daysOfWeek.all { it.isChecked.not() }
        ) {
            return
        }

        _uiState.update { copy(isLoading = true) }
        val alarmDomain = currentAlarm.toDomain()
        if (currentAlarm.id == null) {
            createAlarm(alarmDomain)
            return
        }
        saveAlarm(alarmDomain)
    }

    fun updateAlarmType(alarmTypeUiModel: AlarmTypeUiModel) {
        _uiState.update {
            val newAlarm = alarm.copy(type = alarmTypeUiModel)
            copy(alarm = newAlarm)
        }
    }

    fun updateAlarmTime(time: LocalTime) {
        _uiState.update {
            val newAlarm = alarm.copy(time = time)
            copy(alarm = newAlarm)
        }
    }

    fun updateAlarmDate(date: LocalDate) {
        _uiState.update {
            if (alarm.type != AlarmTypeUiModel.NON_REPEAT) return
            val newAlarm = alarm.copy(date = date)
            copy(alarm = newAlarm)
        }
    }

    fun updateDaysOfWeek(dayOfWeek: DayOfWeekUiModel) {
        _uiState.update {
            val newAlarm =
                alarm.copy(
                    daysOfWeek =
                        alarm.daysOfWeek.map {
                            if (it.dayOfWeek != dayOfWeek.dayOfWeek) return@map it
                            it.copy(isChecked = !it.isChecked)
                        },
                )
            copy(alarm = newAlarm)
        }
    }

    private fun createAlarm(alarm: Alarm) {
        viewModelScope.launch {
            createAlarmUseCase(ssaid, bottariId, alarm)
                .onSuccess {
                    _uiEvent.value = AlarmUiEvent.AlarmCreateSuccess
                }.onFailure {
                    _uiEvent.value = AlarmUiEvent.AlarmCreateFailure
                }
        }

        _uiState.update { copy(isLoading = false) }
    }

    private fun saveAlarm(alarm: Alarm) {
        viewModelScope.launch {
            saveAlarmUseCase(ssaid, alarm.id!!, alarm)
                .onSuccess {
                    _uiEvent.value = AlarmUiEvent.AlarmSaveSuccess
                }.onFailure {
                    _uiEvent.value = AlarmUiEvent.AlarmSaveFailure
                }
        }

        _uiState.update { copy(isLoading = false) }
    }

    companion object {
        private const val KEY_SSAID = "KEY_SSAID"
        private const val KEY_BOTTARI_ID = "KEY_BOTTARI_ID"
        private const val KEY_ALARM = "KEY_ALARM"
        private const val ERROR_REQUIRE_SSAID = "[ERROR] SSAID가 존재하지 않습니다."
        private const val ERROR_REQUIRE_BOTTARI_ID = "[ERROR] 보따리 ID가 존재하지 않습니다."

        fun Factory(
            ssaid: String,
            bottariId: Long?,
            alarm: AlarmUiModel?,
        ): ViewModelProvider.Factory =
            viewModelFactory {
                initializer {
                    val stateHandle = createSavedStateHandle()
                    stateHandle[KEY_ALARM] = alarm
                    stateHandle[KEY_SSAID] = ssaid
                    stateHandle[KEY_BOTTARI_ID] = bottariId
                    AlarmEditViewModel(
                        stateHandle = stateHandle,
                        createAlarmUseCase = UseCaseProvider.createAlarmUseCase,
                        saveAlarmUseCase = UseCaseProvider.saveAlarmUseCase,
                    )
                }
            }
    }
}
