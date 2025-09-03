package com.bottari.data.mapper

import com.bottari.data.model.item.FetchChecklistResponse
import com.bottari.data.model.team.TeamBottariItemResponse
import com.bottari.domain.model.bottari.BottariItem
import com.bottari.domain.model.bottari.BottariItemType
import com.bottari.domain.model.bottari.ChecklistItem

object BottariItemMapper {
    fun FetchChecklistResponse.toDomain(): ChecklistItem =
        ChecklistItem(
            id = id,
            name = name,
            isChecked = isChecked,
        )

    fun TeamBottariItemResponse.toDomain(type: BottariItemType): BottariItem =
        BottariItem(
            id = itemId,
            name = name,
            type = type,
        )
}
