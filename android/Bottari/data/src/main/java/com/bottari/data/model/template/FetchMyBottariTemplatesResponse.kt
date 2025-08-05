package com.bottari.data.model.template

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class FetchMyBottariTemplatesResponse(
    @SerialName("author")
    val author: String,
    @SerialName("id")
    val id: Long,
    @SerialName("items")
    val items: List<FetchMyBottariTemplateItemResponse>,
    @SerialName("title")
    val title: String,
    @SerialName("createdAt")
    val createdAt: String,
    @SerialName("takenCount")
    val takenCount: Int,
)
