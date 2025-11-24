package com.bottari.core.domain.model.notification

import com.bottari.core.domain.model.alarm.Alarm

data class Notification(
    val bottariId: Long,
    val bottariTitle: String,
    val alarm: Alarm,
)
