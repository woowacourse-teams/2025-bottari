package com.bottari.data.model.remote.bottari.template

import com.bottari.domain.model.bottari.template.BottariTemplate
import com.bottari.domain.model.bottari.template.BottariTemplateItem
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
) {
    fun toDomain(): BottariTemplate =
        BottariTemplate(
            id = id,
            title = title,
            items = items.map { it.toDomain() },
            author = author,
            takenCount = takenCount,
        )
}

@Serializable
data class BottariTemplateItemFetchResponse(
    @SerialName("id")
    val id: Long,
    @SerialName("name")
    val name: String,
) {
    fun toDomain(): BottariTemplateItem =
        BottariTemplateItem(
            id = id,
            name = name,
        )
}
