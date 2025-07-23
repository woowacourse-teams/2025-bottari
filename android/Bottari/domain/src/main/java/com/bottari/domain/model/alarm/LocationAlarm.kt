package com.bottari.domain.model.alarm

data class LocationAlarm(
    val latitude: Double,
    val longitude: Double,
    val radius: Int,
    val isActive: Boolean,
)
