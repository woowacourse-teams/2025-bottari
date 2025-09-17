package com.bottari.data.model.remote.bottari

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BottariTitleUpdateRequest(
    @SerialName("title")
    val title: String,
)
