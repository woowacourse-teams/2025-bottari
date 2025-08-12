package com.bottari.data.mapper

import com.bottari.data.model.team.FetchTeamBottariChecklistResponse
import com.bottari.data.model.team.TeamMemberItemResponse
import com.bottari.domain.model.team.TeamBottariCheckList
import com.bottari.domain.model.team.TeamMemberItem

object TeamMapper {
    fun TeamMemberItemResponse.toDomain(): TeamMemberItem =
        TeamMemberItem(
            id = id,
            name = name,
            isChecked = isChecked,
        )

    fun FetchTeamBottariChecklistResponse.toDomain(): TeamBottariCheckList =
        TeamBottariCheckList(
            sharedItems = sharedItems.map { it.toDomain() },
            assignedItems = assignedItems.map { it.toDomain() },
            personalItems = personalItems.map { it.toDomain() },
        )
}
