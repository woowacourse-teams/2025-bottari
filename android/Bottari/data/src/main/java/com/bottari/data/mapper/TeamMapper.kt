package com.bottari.data.mapper

import com.bottari.data.model.team.bottari.item.response.TeamBottariItemChecklistFetchResponse
import com.bottari.data.model.team.bottari.item.response.TeamChecklistItemResponse
import com.bottari.domain.model.bottari.ChecklistItem
import com.bottari.domain.model.team.TeamBottariCheckList

object TeamMapper {
    fun TeamBottariItemChecklistFetchResponse.toDomain(): TeamBottariCheckList =
        TeamBottariCheckList(
            sharedItems = sharedItems.map { it.toDomain() },
            assignedItems = assignedItems.map { it.toDomain() },
            personalItems = personalItems.map { it.toDomain() },
        )

    fun TeamChecklistItemResponse.toDomain(): ChecklistItem =
        ChecklistItem(
            id = id,
            name = name,
            isChecked = isChecked,
        )
}
