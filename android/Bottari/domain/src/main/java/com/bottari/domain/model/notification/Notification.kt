package com.bottari.domain.model.notification

import com.bottari.domain.model.alarm.Alarm

data class Notification(
    val bottariId: Long,
    val bottariTitle: String,
    val alarm: Alarm,
)
