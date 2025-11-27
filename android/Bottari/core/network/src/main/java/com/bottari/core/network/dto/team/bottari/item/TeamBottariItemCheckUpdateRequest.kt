package com.bottari.core.network.dto.team.bottari.item

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TeamBottariItemCheckUpdateRequest(
    @SerialName("type")
    val type: String,
)
