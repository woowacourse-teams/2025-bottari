package com.bottari.data.mapper.bottari.item

import com.bottari.data.model.bottari.BottariItemFetchResponse
import com.bottari.data.model.bottari.item.ItemFetchResponse
import com.bottari.data.model.team.member.TeamMemberStatusBottariItemFetchResponse
import com.bottari.domain.model.bottari.item.BottariItem
import com.bottari.domain.model.bottari.item.ChecklistItem
import com.bottari.domain.model.team.bottari.item.TeamBottariItemType

object BottariItemMapper {
    fun ItemFetchResponse.toChecklistItem(): ChecklistItem =
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

    fun TeamMemberStatusBottariItemFetchResponse.toChecklistItem() =
        ChecklistItem(
            id = id,
            name = name,
            isChecked = isChecked,
        )
}
