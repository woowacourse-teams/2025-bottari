package com.bottari.presentation.model.template

import com.bottari.domain.model.bottari.template.BottariTemplateItem

data class BottariTemplateItemUiModel(
    val id: Long,
    val name: String,
) {
    companion object {
        fun fromDomain(bottariTemplateItem: BottariTemplateItem): BottariTemplateItemUiModel =
            BottariTemplateItemUiModel(
                id = bottariTemplateItem.id,
                name = bottariTemplateItem.name,
            )
    }
}
