package com.bottari.presentation.model.template

import com.bottari.domain.model.bottari.template.BottariTemplate

data class BottariTemplateUiModel(
    val id: Long,
    val title: String,
    val items: List<BottariTemplateItemUiModel>,
    val author: String,
    val takenCount: Int,
) {
    companion object {
        fun fromDomain(bottariTemplate: BottariTemplate): BottariTemplateUiModel =
            BottariTemplateUiModel(
                id = bottariTemplate.id,
                title = bottariTemplate.title,
                items =
                    bottariTemplate.items.map { item ->
                        BottariTemplateItemUiModel.fromDomain(
                            item,
                        )
                    },
                author = bottariTemplate.author,
                takenCount = bottariTemplate.takenCount,
            )
    }
}
