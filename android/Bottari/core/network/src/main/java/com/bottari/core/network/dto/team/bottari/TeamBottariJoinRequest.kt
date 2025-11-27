package com.bottari.core.network.dto.team.bottari

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TeamBottariJoinRequest(
    @SerialName("inviteCode")
    val inviteCode: String,
)
