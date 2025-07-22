package com.bottari.presentation.view.edit.alarm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bottari.presentation.model.AlarmType
import com.bottari.presentation.model.AlarmUiModel
import com.bottari.presentation.model.DayOfWeekUiModel
import java.time.LocalDate
import java.time.LocalTime

class AlarmEditViewModel : ViewModel() {
    private val _alarm: MutableLiveData<AlarmUiModel> =
        MutableLiveData(AlarmUiModel.DEFAULT_ALARM_UI_MODEL)
    val alarm: LiveData<AlarmUiModel> get() = _alarm

    fun saveAlarm() {
    }

    fun updateAlarmType(alarmType: AlarmType) {
        val currentAlarm = _alarm.value ?: return
        _alarm.value = currentAlarm.copy(type = alarmType)
    }

    fun updateAlarmTime(time: LocalTime) {
        val currentAlarm = _alarm.value ?: return
        _alarm.value = currentAlarm.copy(time = time)
    }

    fun updateAlarmDate(date: LocalDate) {
        val currentAlarm = _alarm.value ?: return
        when (currentAlarm.type) {
            AlarmType.NON_REPEAT -> {
                _alarm.value = currentAlarm.copy(date = date)
            }

            AlarmType.EVERYDAY_REPEAT,
            AlarmType.EVERYWEEK_REPEAT,
            -> return
        }
    }

    fun selectDayOfWeek(dayOfWeek: DayOfWeekUiModel) {
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
}
