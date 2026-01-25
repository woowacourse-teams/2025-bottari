package com.bottari.core.ui.model.template

import com.bottari.core.domain.model.bottari.template.BookmarkTemplate

data class BookmarkedTemplateUiModel(
    val id: Long,
    val templateId: Long,
    val title: String,
    val description: String,
    val items: List<String>,
    val hashtags: List<String>,
) {
    companion object {
        fun fromBookmark(template: BookmarkTemplate): BookmarkedTemplateUiModel =
            BookmarkedTemplateUiModel(
                id = template.id,
                templateId = template.templateId,
                title = template.title,
                description = template.description,
                items = template.items,
                hashtags = template.hashtags,
            )
    }
}
