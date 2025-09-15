package com.bottari.data.model.team.member

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TeamMemberNameFetchResponse(
    @SerialName("id")
    val id: Long,
    @SerialName("name")
    val name: String,
)
