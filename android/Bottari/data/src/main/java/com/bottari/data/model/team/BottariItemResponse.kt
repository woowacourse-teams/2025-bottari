package com.bottari.data.model.team

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BottariItemResponse(
    @SerialName("id")
    val itemId: Long,
    @SerialName("name")
    val name: String,
)
