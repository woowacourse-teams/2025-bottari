package com.bottari.data.model.remote.bottari.template

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BottariTemplateCreateRequest(
    @SerialName("title")
    val title: String,
    @SerialName("description")
    val description: String,
    @SerialName("bottariTemplateItems")
    val bottariTemplateItems: List<String>,
    @SerialName("hashtagNames")
    val hashtagNames: List<String>,
)
