package com.bottari.data.model.bottari.template

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BottariTemplateFetchResponse(
    @SerialName("id")
    val id: Long,
    @SerialName("title")
    val title: String,
    @SerialName("items")
    val items: List<BottariTemplateItemFetchResponse>,
    @SerialName("author")
    val author: String,
    @SerialName("createdAt")
    val createdAt: String,
    @SerialName("takenCount")
    val takenCount: Int,
)

@Serializable
data class BottariTemplateItemFetchResponse(
    @SerialName("id")
    val id: Long,
    @SerialName("name")
    val name: String,
)
