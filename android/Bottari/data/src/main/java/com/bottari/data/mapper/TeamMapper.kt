package com.bottari.data.mapper

import com.bottari.data.mapper.BottariItemMapper.toDomain
import com.bottari.data.model.team.FetchTeamBottariChecklistResponse
import com.bottari.domain.model.team.TeamBottariCheckList

object TeamMapper {
    fun FetchTeamBottariChecklistResponse.toDomain(): TeamBottariCheckList =
        TeamBottariCheckList(
            sharedItems = sharedItems.map { it.toDomain() },
            assignedItems = assignedItems.map { it.toDomain() },
            personalItems = personalItems.map { it.toDomain() },
        )
}
