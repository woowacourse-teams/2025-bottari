package com.bottari.presentation.model

import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalTime

data class AlarmUiModel(
    val id: Long? = null,
    val type: AlarmTypeUiModel,
    val time: LocalTime,
    val date: LocalDate,
    val daysOfWeek: List<DayOfWeekUiModel>,
    val locationAlarm: LocationAlarmUiModel? = null,
) {
    companion object {
        val DEFAULT_ALARM_UI_MODEL =
            AlarmUiModel(
                type = AlarmTypeUiModel.NON_REPEAT,
                time = LocalTime.now(),
                date = LocalDate.now(),
                daysOfWeek = DayOfWeek.entries.map { DayOfWeekUiModel(it, false) },
            )
    }
}
