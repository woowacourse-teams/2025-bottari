package com.bottari.data.mapper.bottari.template

import com.bottari.data.model.bottari.template.BottariTemplateItemResponse
import com.bottari.data.model.bottari.template.FetchBottariTemplateResponse
import com.bottari.domain.model.bottari.template.BottariTemplate
import com.bottari.domain.model.bottari.template.BottariTemplateItem

object BottariTemplateMapper {
    fun FetchBottariTemplateResponse.toBottariTemplate(): BottariTemplate =
        BottariTemplate(
            id = id,
            title = title,
            items = items.map { it.toBottariTemplateItem() },
            author = author,
            takenCount = takenCount,
        )

    private fun BottariTemplateItemResponse.toBottariTemplateItem(): BottariTemplateItem =
        BottariTemplateItem(
            id = id,
            name = name,
        )
}
