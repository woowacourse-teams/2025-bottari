package com.bottari.data.model.team.member

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TeamMemberFetchResponse(
    @SerialName("inviteCode")
    val inviteCode: String,
    @SerialName("teamMemberCount")
    val teamMemberCount: Int,
    @SerialName("ownerName")
    val ownerName: String,
    @SerialName("teamMemberNames")
    val teamMemberNames: List<String>,
)
