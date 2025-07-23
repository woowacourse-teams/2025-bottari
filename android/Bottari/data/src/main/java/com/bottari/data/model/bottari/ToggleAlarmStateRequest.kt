package com.bottari.data.model.bottari

class ToggleAlarmStateRequest(
    val id: Long,
    val ssaid: String,
    val active: String = "active",
)
