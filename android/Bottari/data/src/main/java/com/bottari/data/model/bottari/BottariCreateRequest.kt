package com.bottari.data.model.bottari

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BottariCreateRequest(
    @SerialName("title")
    val title: String,
)
