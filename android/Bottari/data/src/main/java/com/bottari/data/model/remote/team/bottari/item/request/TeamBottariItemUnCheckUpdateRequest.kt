package com.bottari.data.model.remote.team.bottari.item.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TeamBottariItemUnCheckUpdateRequest(
    @SerialName("type")
    val type: String,
)
