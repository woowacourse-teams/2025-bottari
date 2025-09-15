package com.bottari.data.model.alarm.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AlarmLocationResponse(
    @SerialName("isActive")
    val isActive: Boolean,
    @SerialName("latitude")
    val latitude: Double,
    @SerialName("longitude")
    val longitude: Double,
    @SerialName("radius")
    val radius: Int,
)
