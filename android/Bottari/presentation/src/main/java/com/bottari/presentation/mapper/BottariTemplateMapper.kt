package com.bottari.presentation.mapper

import com.bottari.domain.model.bottari.template.BottariTemplate
import com.bottari.domain.model.bottari.template.BottariTemplateItem
import com.bottari.presentation.model.BottariTemplateItemUiModel
import com.bottari.presentation.model.BottariTemplateUiModel

object BottariTemplateMapper {
    fun BottariTemplate.toUiModel(): BottariTemplateUiModel =
        BottariTemplateUiModel(
            id = id,
            title = title,
            items = items.map { item -> item.toUiModel() },
            author = author,
            takenCount = takenCount,
        )

    fun BottariTemplateItem.toUiModel(): BottariTemplateItemUiModel =
        BottariTemplateItemUiModel(
            id = id,
            name = name,
        )
}
