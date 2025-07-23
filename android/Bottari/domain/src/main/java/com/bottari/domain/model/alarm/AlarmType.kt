package com.bottari.domain.model.alarm

import java.time.LocalDate

sealed class AlarmType {
    data class NonRepeat(
        val date: LocalDate,
    ) : AlarmType()

    data object EveryDayRepeat : AlarmType()

    data class EveryWeekRepeat(
        val daysOfWeek: List<Int>,
    ) : AlarmType()
}
