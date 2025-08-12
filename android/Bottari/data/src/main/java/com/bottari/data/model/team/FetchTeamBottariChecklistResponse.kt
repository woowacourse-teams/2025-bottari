package com.bottari.data.model.team

import kotlinx.serialization.Serializable

@Serializable
data class FetchTeamBottariChecklistResponse(
    val sharedItems: List<TeamMemberItemResponse>,
    val assignedItems: List<TeamMemberItemResponse>,
    val personalItems: List<TeamMemberItemResponse>,
)
