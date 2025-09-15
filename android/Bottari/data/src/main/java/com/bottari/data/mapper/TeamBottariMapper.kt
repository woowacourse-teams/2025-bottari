package com.bottari.data.mapper

import com.bottari.data.mapper.AlarmMapper.toDomain
import com.bottari.data.mapper.BottariItemMapper.toDomain
import com.bottari.data.model.team.bottari.FetchTeamBottariStatusItemResponse
import com.bottari.data.model.team.bottari.FetchTeamBottariStatusResponse
import com.bottari.data.model.team.bottari.TeamBottariFetchDetailResponse
import com.bottari.data.model.team.bottari.TeamBottariFetchResponse
import com.bottari.data.model.team.bottari.item.AssignedItemsFetchResponse
import com.bottari.data.model.team.bottari.item.PersonalItemsFetchResponse
import com.bottari.data.model.team.bottari.item.SharedItemsFetchResponse
import com.bottari.data.model.team.member.TeamMemberStatusCheckedFetchResponse
import com.bottari.domain.model.bottari.Bottari
import com.bottari.domain.model.bottari.BottariItem
import com.bottari.domain.model.bottari.BottariItemType
import com.bottari.domain.model.bottari.TeamBottari
import com.bottari.domain.model.team.MemberCheckStatus
import com.bottari.domain.model.team.TeamBottariDetail
import com.bottari.domain.model.team.TeamBottariProductStatus
import com.bottari.domain.model.team.TeamBottariStatus
import com.bottari.domain.model.team.TeamMember

object TeamBottariMapper {
    fun TeamBottariFetchResponse.toDomain(): TeamBottari =
        TeamBottari(
            bottari =
                Bottari(
                    id = id,
                    title = title,
                    alarm = alarm?.toDomain(),
                    items = emptyList(),
                ),
            checkedQuantity = checkedItemsCount,
            totalQuantity = totalItemsCount,
            memberCount = memberCount,
        )

    fun TeamBottariFetchDetailResponse.toDomain(): TeamBottariDetail =
        TeamBottariDetail(
            bottari =
                Bottari(
                    id = bottariId,
                    title = title,
                    alarm = alarm?.toDomain(),
                    items = emptyList(),
                ),
            personalItems = personalItems.map { it.toDomain(BottariItemType.PERSONAL) },
            assignedItems = assignedItems.map { it.toDomain(BottariItemType.ASSIGNED(emptyList())) },
            sharedItems = sharedItems.map { it.toDomain(BottariItemType.SHARED) },
        )

    fun FetchTeamBottariStatusResponse.toDomain(): TeamBottariStatus =
        TeamBottariStatus(
            sharedItems = sharedItems.map { it.toDomain() },
            assignedItems = assignedItems.map { it.toDomain() },
        )

    fun AssignedItemsFetchResponse.toDomain(): BottariItem =
        BottariItem(
            id = id,
            name = name,
            type = BottariItemType.ASSIGNED(assignees.map { it.toDomain() }),
        )

    fun SharedItemsFetchResponse.toDomain(): BottariItem =
        BottariItem(
            id = id,
            name = name,
            type = BottariItemType.SHARED,
        )

    fun PersonalItemsFetchResponse.toDomain(): BottariItem =
        BottariItem(
            id = id,
            name = name,
            type = BottariItemType.PERSONAL,
        )

    private fun AssignedItemsFetchResponse.Assignee.toDomain() =
        TeamMember(
            memberId = memberId,
            nickname = name,
        )

    private fun FetchTeamBottariStatusItemResponse.toDomain(): TeamBottariProductStatus =
        TeamBottariProductStatus(
            id = id,
            name = name,
            memberCheckStatus = memberCheckStatus.map { it.toDomain() },
            checkItemsCount = checkItemsCount,
            totalItemsCount = totalItemsCount,
        )

    private fun TeamMemberStatusCheckedFetchResponse.toDomain(): MemberCheckStatus =
        MemberCheckStatus(
            name = name,
            checked = checked,
        )
}
