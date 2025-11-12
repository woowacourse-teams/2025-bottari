package com.bottari.data.model.remote.team.bottari

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TeamBottariJoinRequest(
    @SerialName("inviteCode")
    val inviteCode: String,
)
