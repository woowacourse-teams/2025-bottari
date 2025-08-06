package com.bottari.presentation.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalTime

@Parcelize
data class AlarmUiModel(
    val id: Long? = null,
    val isActive: Boolean,
    val type: AlarmTypeUiModel,
    val time: LocalTime,
    val date: LocalDate,
    val repeatDays: List<RepeatDayUiModel>,
    val locationAlarm: LocationAlarmUiModel? = null,
) : Parcelable {
    val isRepeatEveryDay: Boolean
        get() = repeatDays.size == DAYS_IN_WEEK

    companion object {
        private const val DAYS_IN_WEEK = 7

        val DEFAULT_ALARM_UI_MODEL =
            AlarmUiModel(
                type = AlarmTypeUiModel.NON_REPEAT,
                isActive = true,
                time = LocalTime.now(),
                date = LocalDate.now(),
                repeatDays = DayOfWeek.entries.map { RepeatDayUiModel(it, false) },
            )
    }
}
