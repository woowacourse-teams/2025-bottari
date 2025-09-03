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

@Serializable
data class FetchMyBottariTemplateItemResponse(
    @SerialName("id")
    val id: Long,
    @SerialName("name")
    val name: String,
)

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
