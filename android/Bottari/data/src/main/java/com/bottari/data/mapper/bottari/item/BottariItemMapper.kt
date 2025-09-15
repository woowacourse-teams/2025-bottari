package com.bottari.data.mapper.bottari.item

import com.bottari.data.model.bottari.BottariItemFetchResponse
import com.bottari.data.model.bottari.item.FetchChecklistResponse
import com.bottari.domain.model.bottari.item.BottariItem
import com.bottari.domain.model.bottari.item.ChecklistItem
import com.bottari.domain.model.team.bottari.item.TeamBottariItemType

object BottariItemMapper {
    fun FetchChecklistResponse.toChecklistItem(): ChecklistItem =
        ChecklistItem(
            id = id,
            name = name,
            isChecked = isChecked,
        )

    fun BottariItemFetchResponse.toBottariItem(): BottariItem =
        BottariItem(
            id = id,
            name = name,
            type = TeamBottariItemType.PERSONAL,
        )
}
