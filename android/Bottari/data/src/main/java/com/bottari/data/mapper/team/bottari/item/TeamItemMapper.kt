package com.bottari.data.mapper.team.bottari.item

import com.bottari.data.mapper.team.member.TeamMembersMapper.toTeamMember
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
    fun TeamBottariItemChecklistFetchResponse.toTeamBottariCheckList(): TeamBottariCheckList =
        TeamBottariCheckList(
            sharedItems = sharedItems.map { it.toChecklistItem() },
            assignedItems = assignedItems.map { it.toChecklistItem() },
            personalItems = personalItems.map { it.toChecklistItem() },
        )

    fun TeamChecklistItemResponse.toChecklistItem(): ChecklistItem =
        ChecklistItem(
            id = id,
            name = name,
            isChecked = isChecked,
        )

    fun AssignedItemsFetchResponse.toBottariItem(): BottariItem =
        BottariItem(
            id = id,
            name = name,
            type = TeamBottariItemType.ASSIGNED(assignees.map { it.toTeamMember() }),
        )

    fun SharedItemsFetchResponse.toBottariItem(): BottariItem =
        BottariItem(
            id = id,
            name = name,
            type = TeamBottariItemType.SHARED,
        )

    fun PersonalItemsFetchResponse.toBottariItem(): BottariItem =
        BottariItem(
            id = id,
            name = name,
            type = TeamBottariItemType.PERSONAL,
        )

    fun TeamBottariDetailFetchItemResponse.toBottariItem(type: TeamBottariItemType): BottariItem =
        BottariItem(
            id = itemId,
            name = name,
            type = type,
        )
}
