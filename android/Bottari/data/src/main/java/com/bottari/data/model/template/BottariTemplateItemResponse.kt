package com.bottari.data.model.template

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BottariTemplateItemResponse(
    @SerialName("id")
    val id: Long,
    @SerialName("name")
    val name: String,
)
