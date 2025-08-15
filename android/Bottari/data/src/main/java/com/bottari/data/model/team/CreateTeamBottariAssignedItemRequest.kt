package com.bottari.data.model.team

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CreateTeamBottariAssignedItemRequest(
    @SerialName("name")
    val name: String,
    @SerialName("teamMemberNames")
    val teamMemberNames: List<String>,
)
