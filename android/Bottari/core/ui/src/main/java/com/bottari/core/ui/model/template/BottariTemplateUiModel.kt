package com.bottari.core.ui.model.template

import com.bottari.core.domain.model.bottari.template.BookmarkTemplate
import com.bottari.core.domain.model.bottari.template.BottariTemplate

data class BottariTemplateUiModel(
    val id: Long,
    val title: String,
    val description: String,
    val items: List<BottariTemplateItemUiModel>,
    val author: String,
    val takenCount: Int,
    val hashtags: List<BottariTemplateHashtagUiModel> = emptyList(),
    val isMarked: Boolean = false,
) {
    fun toDomain(): BookmarkTemplate =
        BookmarkTemplate(
            templateId = id,
            title = title,
            description = description,
            items = items.map { item -> item.name },
            hashtags = hashtags.map { hashtag -> hashtag.name },
        )

    companion object {
        fun fromDomain(bottariTemplate: BottariTemplate): BottariTemplateUiModel =
            BottariTemplateUiModel(
                id = bottariTemplate.id,
                title = bottariTemplate.title,
                description = bottariTemplate.description,
                items = bottariTemplate.items.map(BottariTemplateItemUiModel::fromDomain),
                author = bottariTemplate.author,
                takenCount = bottariTemplate.takenCount,
                hashtags = bottariTemplate.hashtags.map(BottariTemplateHashtagUiModel::fromDomain),
                isMarked = bottariTemplate.isMarked,
            )
    }
}
