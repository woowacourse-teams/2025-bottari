package com.bottari.data.mapper.team.bottari.item

import com.bottari.data.mapper.team.member.TeamMembersMapper.toDomain
import com.bottari.data.model.team.bottari.TeamBottariDetailFetchItemResponse
import com.bottari.data.model.team.bottari.item.response.AssignedItemsFetchResponse
import com.bottari.data.model.team.bottari.item.response.PersonalItemsFetchResponse
import com.bottari.data.model.team.bottari.item.response.SharedItemsFetchResponse
import com.bottari.data.model.team.bottari.item.response.TeamBottariItemChecklistFetchResponse
import com.bottari.data.model.team.bottari.item.response.TeamChecklistItemResponse
import com.bottari.domain.model.bottari.item.BottariItem
import com.bottari.domain.model.bottari.item.ChecklistItem
import com.bottari.domain.model.team.bottari.TeamBottariCheckList
import com.bottari.domain.model.team.bottari.item.TeamBottariItemType

object TeamItemMapper {
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

    fun AssignedItemsFetchResponse.toDomain(): BottariItem =
        BottariItem(
            id = id,
            name = name,
            type = TeamBottariItemType.ASSIGNED(assignees.map { it.toDomain() }),
        )

    fun SharedItemsFetchResponse.toDomain(): BottariItem =
        BottariItem(
            id = id,
            name = name,
            type = TeamBottariItemType.SHARED,
        )

    fun PersonalItemsFetchResponse.toDomain(): BottariItem =
        BottariItem(
            id = id,
            name = name,
            type = TeamBottariItemType.PERSONAL,
        )

    fun TeamBottariDetailFetchItemResponse.toDomain(type: TeamBottariItemType): BottariItem =
        BottariItem(
            id = itemId,
            name = name,
            type = type,
        )
}
