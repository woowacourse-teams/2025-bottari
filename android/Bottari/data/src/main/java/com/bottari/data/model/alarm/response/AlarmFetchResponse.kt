package com.bottari.data.model.alarm.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AlarmFetchResponse(
    @SerialName("id")
    val id: Long,
    @SerialName("isActive")
    val isActive: Boolean,
    @SerialName("location")
    val location: AlarmLocationResponse?,
    @SerialName("routine")
    val routine: AlarmRoutineResponse,
)
