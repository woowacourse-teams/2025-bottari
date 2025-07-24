package com.bottari.data.model.template


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CreateBottariTemplateRequest(
    @SerialName("bottariTemplateItems")
    val bottariTemplateItems: List<String>,
    @SerialName("title")
    val title: String
)
