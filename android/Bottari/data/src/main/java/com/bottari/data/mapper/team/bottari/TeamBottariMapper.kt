package com.bottari.data.mapper.team.bottari

import com.bottari.data.mapper.alarm.AlarmMapper.toAlarm
import com.bottari.data.mapper.team.bottari.item.TeamItemMapper.toBottariItem
import com.bottari.data.mapper.team.member.TeamMembersMapper.toMemberCheckStatus
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
    fun TeamBottariFetchResponse.toTeamBottari(): TeamBottari =
        TeamBottari(
            bottari =
                Bottari(
                    id = id,
                    title = title,
                    alarm = alarm?.toAlarm(),
                    items = emptyList(),
                ),
            checkedQuantity = checkedItemsCount,
            totalQuantity = totalItemsCount,
            memberCount = HeadCount(memberCount),
        )

    fun TeamBottariDetailFetchResponse.toTeamBottariDetail(): TeamBottariDetail =
        TeamBottariDetail(
            bottari =
                Bottari(
                    id = bottariId,
                    title = title,
                    alarm = alarm?.toAlarm(),
                    items = emptyList(),
                ),
            personalItems = personalItems.map { it.toBottariItem(TeamBottariItemType.PERSONAL) },
            assignedItems = assignedItems.map { it.toBottariItem(TeamBottariItemType.ASSIGNED(emptyList())) },
            sharedItems = sharedItems.map { it.toBottariItem(TeamBottariItemType.SHARED) },
        )

    fun FetchTeamBottariStatusResponse.toTeamBottariStatus(): TeamBottariStatus =
        TeamBottariStatus(
            sharedItems = sharedItems.map { it.toTeamBottariProductStatus() },
            assignedItems = assignedItems.map { it.toTeamBottariProductStatus() },
        )

    private fun FetchTeamBottariStatusItemResponse.toTeamBottariProductStatus(): TeamBottariProductStatus =
        TeamBottariProductStatus(
            id = id,
            name = name,
            memberCheckStatus = memberCheckStatus.map { it.toMemberCheckStatus() },
            itemCount =
                BottariItemCount(
                    checkedQuantity = checkItemsCount,
                    totalQuantity = totalItemsCount,
                ),
        )
}
