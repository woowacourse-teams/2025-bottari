package com.bottari.data.mapper

import com.bottari.data.model.template.BottariTemplateItemResponse
import com.bottari.data.model.template.FetchBottariTemplateResponse
import com.bottari.domain.model.template.BottariTemplate
import com.bottari.domain.model.template.BottariTemplateItem

object BottariTemplateMapper {
    fun FetchBottariTemplateResponse.toDomain(): BottariTemplate =
        BottariTemplate(
            id = id,
            title = title,
            items = items.map { it.toDomain() },
            author = author,
        )

    private fun BottariTemplateItemResponse.toDomain(): BottariTemplateItem =
        BottariTemplateItem(
            id = id,
            name = name,
        )
}
