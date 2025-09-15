package com.bottari.data.mapper.bottari.template

import com.bottari.data.model.bottari.template.BottariTemplateFetchResponse
import com.bottari.data.model.bottari.template.BottariTemplateItemFetchResponse
import com.bottari.domain.model.bottari.template.BottariTemplate
import com.bottari.domain.model.bottari.template.BottariTemplateItem

object BottariTemplateMapper {
    fun BottariTemplateFetchResponse.toBottariTemplate(): BottariTemplate =
        BottariTemplate(
            id = id,
            title = title,
            items = items.map { it.toBottariTemplateItem() },
            author = author,
            takenCount = takenCount,
        )

    private fun BottariTemplateItemFetchResponse.toBottariTemplateItem(): BottariTemplateItem =
        BottariTemplateItem(
            id = id,
            name = name,
        )
}
