package com.bottari.data.mapper

import com.bottari.data.model.bottari.item.FetchChecklistResponse
import com.bottari.data.model.team.bottari.TeamBottariDetailFetchItemResponse
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

    fun TeamBottariDetailFetchItemResponse.toDomain(type: BottariItemType): BottariItem =
        BottariItem(
            id = itemId,
            name = name,
            type = type,
        )
}
