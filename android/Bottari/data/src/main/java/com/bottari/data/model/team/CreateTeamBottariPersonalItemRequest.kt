package com.bottari.data.model.team

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CreateTeamBottariPersonalItemRequest(
    @SerialName("name")
    val name: String,
)
