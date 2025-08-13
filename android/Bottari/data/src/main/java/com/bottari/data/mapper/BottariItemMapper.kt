package com.bottari.data.mapper

import com.bottari.data.model.item.FetchChecklistResponse
import com.bottari.domain.model.bottari.ChecklistItem

object BottariItemMapper {
    fun FetchChecklistResponse.toDomain(): ChecklistItem =
        ChecklistItem(
            id = id,
            name = name,
            isChecked = isChecked,
        )
}
