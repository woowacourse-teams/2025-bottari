package com.bottari.data.model.remote.bottari.template

import com.bottari.domain.model.bottari.template.BottariTemplate
import com.bottari.domain.model.bottari.template.BottariTemplateHashtag
import com.bottari.domain.model.bottari.template.BottariTemplateItem
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BottariTemplateFetchResponse(
    @SerialName("id")
    val id: Long,
    @SerialName("title")
    val title: String,
    @SerialName("description")
    val description: String,
    @SerialName("items")
    val items: List<Item>,
    @SerialName("author")
    val author: String,
    @SerialName("createdAt")
    val createdAt: String,
    @SerialName("takenCount")
    val takenCount: Int,
    @SerialName("hashtags")
    val hashtags: List<Hashtag>,
) {
    @Serializable
    data class Item(
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

    @Serializable
    data class Hashtag(
        @SerialName("id")
        val id: Long,
        @SerialName("name")
        val name: String,
    ) {
        fun toDomain(): BottariTemplateHashtag =
            BottariTemplateHashtag(
                id = id,
                name = name,
            )
    }

    fun toDomain(): BottariTemplate =
        BottariTemplate(
            id = id,
            title = title,
            description = description,
            items = items.map { it.toDomain() },
            author = author,
            takenCount = takenCount,
            hashtags = hashtags.map { it.toDomain() },
        )
}
