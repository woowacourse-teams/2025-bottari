package com.bottari.data.model.team.bottari

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CreateTeamBottariRequest(
    @SerialName("title")
    val title: String,
)

@Serializable
data class JoinTeamBottariRequest(
    @SerialName("inviteCode")
    val inviteCode: String,
)
