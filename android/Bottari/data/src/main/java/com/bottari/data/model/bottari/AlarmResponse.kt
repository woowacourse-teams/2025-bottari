package com.bottari.data.model.bottari

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AlarmResponse(
    @SerialName("location")
    val locationResponse: LocationResponse?,
    @SerialName("routine")
    val routine: RoutineResponse,
)
