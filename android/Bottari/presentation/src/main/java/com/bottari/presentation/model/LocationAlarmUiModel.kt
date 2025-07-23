package com.bottari.presentation.model

data class LocationAlarmUiModel(
    val latitude: Double,
    val longitude: Double,
    val radius: Int,
    val isActive: Boolean,
)
