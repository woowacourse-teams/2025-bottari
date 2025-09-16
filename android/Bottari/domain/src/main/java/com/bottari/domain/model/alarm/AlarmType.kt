package com.bottari.domain.model.alarm

import java.time.LocalDate

private const val NON_REPEAT = "NON_REPEAT"
private const val EVERY_DAY_REPEAT = "EVERY_DAY_REPEAT"
private const val EVERY_WEEK_REPEAT = "EVERY_WEEK_REPEAT"
private const val DAYS_IN_WEEK = 7

sealed class AlarmType {
    data class NonRepeat(
        val date: LocalDate,
    ) : AlarmType()

    data class Repeat(
        val repeatDays: List<Int>,
    ) : AlarmType()

    fun toTypeString(): String =
        when (this) {
            is NonRepeat -> NON_REPEAT
            is Repeat -> if (repeatDays.size == DAYS_IN_WEEK) EVERY_DAY_REPEAT else EVERY_WEEK_REPEAT
        }

    fun getAlarmDate(): LocalDate? {
        if (this !is NonRepeat) return null
        return date
    }

    fun getDaysOfWeek(): List<Int> {
        if (this !is Repeat) return emptyList()
        return repeatDays
    }

    fun getAlarmRepeatDays(): List<Int> =
        when (this) {
            is NonRepeat -> emptyList()
            is Repeat -> repeatDays
        }
}
