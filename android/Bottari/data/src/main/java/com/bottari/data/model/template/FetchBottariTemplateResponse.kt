package com.bottari.data.model.template

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class FetchBottariTemplateResponse(
    @SerialName("id")
    val id: Long,
    @SerialName("title")
    val title: String,
    @SerialName("items")
    val items: List<BottariTemplateItemResponse>,
    @SerialName("author")
    val author: String,
    @SerialName("createdAt")
    val createdAt: String,
    @SerialName("takenCount")
    val takenCount: Int,
)
