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
import com.bottari.presentation.base.UiState
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
    private val _alarm: MutableLiveData<AlarmUiModel> =
        MutableLiveData(stateHandle[EXTRA_ALARM])
    val alarm: LiveData<AlarmUiModel> get() = _alarm

    private val _saveState: MutableLiveData<UiState<Unit>> = MutableLiveData()
    val saveState: LiveData<UiState<Unit>> get() = _saveState

    private val ssaid: String by lazy { stateHandle[EXTRA_SSAID]!! }
    private val bottariId: Long by lazy { stateHandle[EXTRA_BOTTARI_ID]!! }

    fun updateAlarm() {
        val currentAlarm = _alarm.value?.toDomain() ?: return
        if (currentAlarm.id == null) {
            createAlarm(currentAlarm)
            return
        }
        saveAlarm(currentAlarm)
    }

    fun updateAlarmType(alarmTypeUiModel: AlarmTypeUiModel) {
        val currentAlarm = _alarm.value ?: return
        _alarm.value = currentAlarm.copy(type = alarmTypeUiModel)
    }

    fun updateAlarmTime(time: LocalTime) {
        val currentAlarm = _alarm.value ?: return
        _alarm.value = currentAlarm.copy(time = time)
    }

    fun updateAlarmDate(date: LocalDate) {
        val currentAlarm = _alarm.value ?: return
        if (currentAlarm.type != AlarmTypeUiModel.NON_REPEAT) return
        _alarm.value = currentAlarm.copy(date = date)
    }

    fun updateDaysOfWeek(dayOfWeek: DayOfWeekUiModel) {
        val currentAlarm = _alarm.value ?: return
        _alarm.value =
            currentAlarm.copy(
                daysOfWeek =
                    currentAlarm.daysOfWeek.map {
                        if (it.dayOfWeek != dayOfWeek.dayOfWeek) return@map it
                        it.copy(isChecked = !it.isChecked)
                    },
            )
    }

    private fun createAlarm(alarm: Alarm) {
        viewModelScope.launch {
            createAlarmUseCase(ssaid, bottariId, alarm)
                .onSuccess {
                    _saveState.value = UiState.Success(Unit)
                }.onFailure { error ->
                    _saveState.value = UiState.Failure(error.message)
                }
        }
    }

    private fun saveAlarm(alarm: Alarm) {
        viewModelScope.launch {
            saveAlarmUseCase(ssaid, alarm.id!!, alarm)
                .onSuccess {
                    _saveState.value = UiState.Success(Unit)
                }.onFailure { error ->
                    _saveState.value = UiState.Failure(error.message)
                }
        }
    }

    companion object {
        private const val EXTRA_SSAID = "EXTRA_SSAID"
        private const val EXTRA_BOTTARI_ID = "EXTRA_BOTTARI_ID"
        private const val EXTRA_ALARM = "EXTRA_ALARM"

        fun Factory(
            ssaid: String,
            bottariId: Long?,
            alarm: AlarmUiModel?,
        ): ViewModelProvider.Factory =
            viewModelFactory {
                initializer {
                    val stateHandle = createSavedStateHandle()
                    stateHandle[EXTRA_ALARM] = alarm
                    stateHandle[EXTRA_SSAID] = ssaid
                    stateHandle[EXTRA_BOTTARI_ID] = bottariId
                    AlarmEditViewModel(
                        stateHandle = stateHandle,
                        createAlarmUseCase = UseCaseProvider.createAlarmUseCase,
                        saveAlarmUseCase = UseCaseProvider.saveAlarmUseCase,
                    )
                }
            }
    }
}
