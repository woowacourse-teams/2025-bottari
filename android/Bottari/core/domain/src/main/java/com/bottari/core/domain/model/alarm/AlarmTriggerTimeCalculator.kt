package com.bottari.core.domain.model.alarm

import com.bottari.core.domain.extension.toTimeMillis
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import javax.inject.Inject

class AlarmTriggerTimeCalculator @Inject constructor() {
    fun calculate(
        alarm: Alarm,
        baseDateTime: LocalDateTime = LocalDateTime.now(),
    ): Long? {
        if (alarm.isActive.not()) return null

        return when (val type = alarm.alarmType) {
            is AlarmType.NonRepeat ->
                calculateNonRepeat(
                    date = type.date,
                    time = alarm.time,
                    baseDateTime = baseDateTime,
                )

            is AlarmType.Repeat ->
                calculateRepeat(
                    repeatDays = type.repeatDays,
                    time = alarm.time,
                    baseDateTime = baseDateTime,
                )
        }
    }

    private fun calculateNonRepeat(
        date: LocalDate,
        time: LocalTime,
        baseDateTime: LocalDateTime,
    ): Long? {
        val dateTime = LocalDateTime.of(date, time)
        if (dateTime.isBefore(baseDateTime)) return null
        return dateTime.toTimeMillis()
    }

    private fun calculateRepeat(
        repeatDays: List<Int>,
        time: LocalTime,
        baseDateTime: LocalDateTime,
    ): Long? {
        val baseDate = baseDateTime.toLocalDate()
        val availableDays = repeatDays.map(DayOfWeek::of)

        val isValid =
            baseDate.dayOfWeek in availableDays && baseDateTime.toLocalTime().isBefore(time)
        if (isValid) return LocalDateTime.of(baseDate, time).toTimeMillis()

        val nextDate =
            availableDays
                .map { dayOfWeek ->
                    val daysUntil =
                        (dayOfWeek.value - baseDateTime.dayOfWeek.value + DAYS_IN_WEEK) % DAYS_IN_WEEK
                    val adjustedDaysUntil = if (daysUntil == 0) DAYS_IN_WEEK else daysUntil
                    baseDate.plusDays(adjustedDaysUntil.toLong())
                }.minByOrNull { date -> date.toEpochDay() }
        return LocalDateTime.of(nextDate, time).toTimeMillis()
    }

    companion object {
        private const val DAYS_IN_WEEK = 7
    }
}
