package com.bottari.data.mapper.bottari.item

import com.bottari.data.model.bottari.BottariItemFetchResponse
import com.bottari.data.model.bottari.item.FetchChecklistResponse
import com.bottari.domain.model.bottari.item.BottariItem
import com.bottari.domain.model.bottari.item.ChecklistItem
import com.bottari.domain.model.team.bottari.item.TeamBottariItemType

object BottariItemMapper {
    fun FetchChecklistResponse.toDomain(): ChecklistItem =
        ChecklistItem(
            id = id,
            name = name,
            isChecked = isChecked,
        )

    fun BottariItemFetchResponse.toDomain(): BottariItem =
        BottariItem(
            id = id,
            name = name,
            type = TeamBottariItemType.PERSONAL,
        )
}
