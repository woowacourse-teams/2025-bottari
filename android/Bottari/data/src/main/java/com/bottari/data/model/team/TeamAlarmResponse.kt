package com.bottari.data.model.team

import com.bottari.data.model.bottari.RoutineResponse
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TeamAlarmResponse(
    @SerialName("id")
    val id: Long,
    @SerialName("isActive")
    val isActive: Boolean,
    @SerialName("location")
    val location: TeamAlarmLocationResponse?,
    @SerialName("routine")
    val routine: RoutineResponse,
)
