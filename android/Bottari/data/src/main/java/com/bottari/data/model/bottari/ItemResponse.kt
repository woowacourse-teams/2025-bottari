package com.bottari.data.model.bottari

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ItemResponse(
    @SerialName("id")
    val id: Int,
    @SerialName("name")
    val name: String,
)
