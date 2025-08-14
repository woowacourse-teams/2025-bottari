package com.bottari.data.mapper

import com.bottari.data.mapper.AlarmMapper.toDomain
import com.bottari.data.mapper.BottariItemMapper.toDomain
import com.bottari.data.model.team.FetchTeamBottariDetailResponse
import com.bottari.data.model.team.FetchTeamBottariResponse
import com.bottari.domain.model.bottari.BottariItemType
import com.bottari.domain.model.bottari.TeamBottari
import com.bottari.domain.model.team.TeamBottariDetail

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
}
