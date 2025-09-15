package com.bottari.data.mapper.team.bottari

import com.bottari.data.mapper.alarm.AlarmMapper.toDomain
import com.bottari.data.mapper.team.bottari.item.TeamItemMapper.toDomain
import com.bottari.data.mapper.team.member.TeamMembersMapper.toDomain
import com.bottari.data.model.team.bottari.FetchTeamBottariStatusItemResponse
import com.bottari.data.model.team.bottari.FetchTeamBottariStatusResponse
import com.bottari.data.model.team.bottari.TeamBottariDetailFetchResponse
import com.bottari.data.model.team.bottari.TeamBottariFetchResponse
import com.bottari.domain.model.bottari.Bottari
import com.bottari.domain.model.bottari.item.BottariItemCount
import com.bottari.domain.model.team.bottari.TeamBottari
import com.bottari.domain.model.team.bottari.TeamBottariDetail
import com.bottari.domain.model.team.bottari.TeamBottariProductStatus
import com.bottari.domain.model.team.bottari.TeamBottariStatus
import com.bottari.domain.model.team.bottari.item.TeamBottariItemType
import com.bottari.domain.model.team.member.HeadCount

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
            memberCount = HeadCount(memberCount),
        )

    fun TeamBottariDetailFetchResponse.toDomain(): TeamBottariDetail =
        TeamBottariDetail(
            bottari =
                Bottari(
                    id = bottariId,
                    title = title,
                    alarm = alarm?.toDomain(),
                    items = emptyList(),
                ),
            personalItems = personalItems.map { it.toDomain(TeamBottariItemType.PERSONAL) },
            assignedItems = assignedItems.map { it.toDomain(TeamBottariItemType.ASSIGNED(emptyList())) },
            sharedItems = sharedItems.map { it.toDomain(TeamBottariItemType.SHARED) },
        )

    fun FetchTeamBottariStatusResponse.toDomain(): TeamBottariStatus =
        TeamBottariStatus(
            sharedItems = sharedItems.map { it.toDomain() },
            assignedItems = assignedItems.map { it.toDomain() },
        )

    private fun FetchTeamBottariStatusItemResponse.toDomain(): TeamBottariProductStatus =
        TeamBottariProductStatus(
            id = id,
            name = name,
            memberCheckStatus = memberCheckStatus.map { it.toDomain() },
            itemCount =
                BottariItemCount(
                    checkedQuantity = checkItemsCount,
                    totalQuantity = totalItemsCount,
                ),
        )
}
