package com.bottari.presentation.mapper

import com.bottari.domain.model.template.BottariTemplate
import com.bottari.domain.model.template.BottariTemplateItem
import com.bottari.presentation.model.BottariTemplateItemUiModel
import com.bottari.presentation.model.BottariTemplateUiModel

object BottariTemplateMapper {
    fun BottariTemplate.toUiModel(): BottariTemplateUiModel =
        BottariTemplateUiModel(
            id = id,
            title = title,
            items = items.map { item -> item.toUiModel() },
            author = author,
        )

    fun BottariTemplateItem.toUiModel(): BottariTemplateItemUiModel =
        BottariTemplateItemUiModel(
            id = id,
            name = name,
        )
}
