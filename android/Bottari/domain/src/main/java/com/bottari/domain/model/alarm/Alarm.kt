package com.bottari.domain.model.alarm

import java.time.LocalTime

data class Alarm(
    val id: Long?,
    val time: LocalTime,
    val alarmType: AlarmType,
    val location: LocationAlarm?,
)
