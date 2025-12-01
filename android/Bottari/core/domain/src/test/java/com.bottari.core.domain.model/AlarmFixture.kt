package com.bottari.core.domain.model

import com.bottari.core.domain.model.alarm.Alarm
import com.bottari.core.domain.model.alarm.AlarmType
import java.time.LocalDate
import java.time.LocalTime

val ACTIVE_REPEAT_ALARM_FIXTURE =
    Alarm(
        id = 1L,
        isActive = true,
        time = LocalTime.of(18, 0),
        alarmType = AlarmType.Repeat(listOf(1, 3, 5)),
        location = null,
    )

val ACTIVE_NON_REPEAT_ALARM_FIXTURE =
    Alarm(
        id = 1L,
        isActive = true,
        time = LocalTime.of(18, 0),
        alarmType = AlarmType.NonRepeat(LocalDate.of(2025, 11, 28)),
        location = null,
    )

val NON_ACTIVE_ALARM_FIXTURE =
    Alarm(
        id = 1L,
        isActive = false,
        time = LocalTime.of(18, 0),
        alarmType = AlarmType.Repeat(listOf(1, 3, 5)),
        location = null,
    )
