package com.bottari.domain.model.alarm

import java.time.LocalDate

sealed class AlarmType {
    data class NonRepeat(
        val date: LocalDate,
    ) : AlarmType()

    data class Repeat(
        val repeatDays: List<Int>,
    ) : AlarmType()
}
