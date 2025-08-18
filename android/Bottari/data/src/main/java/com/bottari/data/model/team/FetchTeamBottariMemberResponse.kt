package com.bottari.data.model.team

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class FetchTeamBottariMemberResponse(
    @SerialName("id")
    val id: Long,
    @SerialName("name")
    val name: String,
)
