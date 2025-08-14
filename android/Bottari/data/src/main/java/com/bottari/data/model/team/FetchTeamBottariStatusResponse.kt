package com.bottari.data.model.team

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class FetchTeamBottariStatusResponse(
    @SerialName("sharedItems")
    val sharedItems: List<TeamProductStatusResponse>,
    @SerialName("assignedItems")
    val assignedItems: List<TeamProductStatusResponse>,
)
