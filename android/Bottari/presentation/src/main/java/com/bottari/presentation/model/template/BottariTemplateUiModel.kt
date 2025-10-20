package com.bottari.presentation.model.template

import com.bottari.domain.model.bottari.template.BottariTemplate

data class BottariTemplateUiModel(
    val id: Long,
    val title: String,
    val description: String,
    val items: List<BottariTemplateItemUiModel>,
    val author: String,
    val takenCount: Int,
    val hashtags: List<BottariTemplateHashtagUiModel> = emptyList(),
) {
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
            )
    }
}
