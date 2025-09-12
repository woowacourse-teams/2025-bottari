package com.bottari.data.model.team.bottari

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TeamBottariCreateRequest(
    @SerialName("title")
    val title: String,
)

@Serializable
data class TeamBottariJoinRequest(
    @SerialName("inviteCode")
    val inviteCode: String,
)
