package com.bottari.presentation.model

import java.time.LocalDate
import java.time.LocalTime

sealed class AlarmTypeUiModel {
    data class NonRepeat(
        val date: LocalDate,
        val time: LocalTime,
    ) : AlarmTypeUiModel()

    data class EveryDayRepeat(
        val time: LocalTime,
    ) : AlarmTypeUiModel()

    data class EveryWeekRepeat(
        val days: List<Int>,
        val time: LocalTime,
    ) : AlarmTypeUiModel()
}
