package com.bottari.data.model.team.bottari.item.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TeamBottariItemDeleteRequest(
    @SerialName("type")
    val type: String,
)
