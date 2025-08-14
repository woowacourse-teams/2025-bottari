package com.bottari.data.mapper

import com.bottari.data.mapper.AlarmMapper.toDomain
import com.bottari.data.mapper.BottariItemMapper.toDomain
import com.bottari.data.mapper.TeamBottariMapper.toDomain
import com.bottari.data.model.team.FetchTeamBottariDetailResponse
import com.bottari.data.model.team.FetchTeamBottariResponse
import com.bottari.data.model.team.FetchTeamBottariStatusResponse
import com.bottari.data.model.team.MemberCheckStatusResponse
import com.bottari.data.model.team.TeamProductStatusResponse
import com.bottari.domain.model.bottari.BottariItemType
import com.bottari.domain.model.bottari.TeamBottari
import com.bottari.domain.model.team.MemberCheckStatus
import com.bottari.domain.model.team.TeamBottariDetail
import com.bottari.domain.model.team.TeamBottariStatus
import com.bottari.domain.model.team.TeamProductStatus

object TeamBottariMapper {
    fun FetchTeamBottariResponse.toDomain(): TeamBottari =
        TeamBottari(
            alarm = alarmResponse?.toDomain(),
            checkedQuantity = checkedItemsCount,
            id = id,
            title = title,
            totalQuantity = totalItemsCount,
            memberCount = memberCount,
        )

    fun FetchTeamBottariDetailResponse.toDomain(): TeamBottariDetail =
        TeamBottariDetail(
            id = bottariId,
            title = title,
            alarm = alarm?.toDomain(),
            personalItems = personalItems.map { it.toDomain(BottariItemType.PERSONAL) },
            assignedItems = assignedItems.map { it.toDomain(BottariItemType.ASSIGNED(emptyList())) },
            sharedItems = sharedItems.map { it.toDomain(BottariItemType.SHARED) },
        )

    fun FetchTeamBottariStatusResponse.toDomain(): TeamBottariStatus =
        TeamBottariStatus(
            sharedItems = sharedItems.map { it.toDomain() },
            assignedItems = assignedItems.map { it.toDomain() },
        )

    private fun TeamProductStatusResponse.toDomain(): TeamProductStatus =
        TeamProductStatus(
            id = id,
            name = name,
            memberCheckStatus = memberCheckStatus.map { it.toDomain() },
            checkItemsCount = checkItemsCount,
            totalItemsCount = totalItemsCount,
        )

    private fun MemberCheckStatusResponse.toDomain(): MemberCheckStatus =
        MemberCheckStatus(
            name = name,
            checked = checked,
        )
}
