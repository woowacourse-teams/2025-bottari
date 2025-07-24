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
    val daysOfWeek: List<DayOfWeekUiModel>,
    val locationAlarm: LocationAlarmUiModel? = null,
) : Parcelable {
    companion object {
        val DEFAULT_ALARM_UI_MODEL =
            AlarmUiModel(
                type = AlarmTypeUiModel.NON_REPEAT,
                isActive = true,
                time = LocalTime.now(),
                date = LocalDate.now(),
                daysOfWeek = DayOfWeek.entries.map { DayOfWeekUiModel(it, false) },
            )
    }
}
