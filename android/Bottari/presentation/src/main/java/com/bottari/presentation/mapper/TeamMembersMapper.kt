package com.bottari.presentation.mapper

import com.bottari.domain.model.team.TeamMemberStatus
import com.bottari.domain.model.team.TeamMembers
import com.bottari.presentation.mapper.BottariMapper.toUiModel
import com.bottari.presentation.model.TeamMemberStatusUiModel
import com.bottari.presentation.model.TeamMemberUiModel

object TeamMembersMapper {
    fun TeamMembers.toUiModel(): List<TeamMemberUiModel> =
        buildList {
            add(TeamMemberUiModel(null, hostName.value, true))
            memberNicknames
                .forEach { nickname ->
                    if (nickname != hostName) add(TeamMemberUiModel(null, nickname.value, false))
                }
        }

    fun TeamMemberStatus.toUiModel(): TeamMemberStatusUiModel =
        TeamMemberStatusUiModel(
            member = TeamMemberUiModel(null, nickname.value, isHost),
            totalItemsCount = totalItemsCount,
            checkedItemsCount = checkedItemsCount,
            sharedItems = sharedItems.map { sharedItem -> sharedItem.toUiModel() },
            assignedItems = assignedItems.map { assignedItem -> assignedItem.toUiModel() },
        )
}
