package com.bottari.data.model.teamItem

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class FetchTeamSharedItemResponse(
    @SerialName("id")
    val id: Long,
    @SerialName("name")
    val name: String,
)
