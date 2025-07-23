package com.bottari.data.model.bottari

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class FindBottariRequest(
    @SerialName("id")
    val id: Long,
    @SerialName("ssaid")
    val ssaid: String,
)
