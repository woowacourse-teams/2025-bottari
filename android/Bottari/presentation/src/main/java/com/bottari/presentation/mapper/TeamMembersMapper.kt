package com.bottari.presentation.mapper

import com.bottari.domain.model.team.TeamMemberStatus
import com.bottari.domain.model.team.TeamMembers
import com.bottari.presentation.mapper.BottariMapper.toUiModel
import com.bottari.presentation.model.TeamMemberStatusUiModel
import com.bottari.presentation.model.TeamMemberUiModel
import kotlin.sequences.forEach

object TeamMembersMapper {
    fun TeamMembers.toUiModel(): List<TeamMemberUiModel> =
        buildList {
            add(TeamMemberUiModel(hostName.value, true))
            memberNicknames
                .asSequence()
                .filter { nickname -> nickname != hostName }
                .forEach { nickname -> add(TeamMemberUiModel(nickname.value, false)) }
        }

    fun TeamMemberStatus.toUiModel(): TeamMemberStatusUiModel =
        TeamMemberStatusUiModel(
            member = TeamMemberUiModel(nickname.value, isHost),
            totalItemsCount = totalItemsCount,
            checkedItemsCount = checkedItemsCount,
            sharedItems = sharedItems.map { sharedItem -> sharedItem.toUiModel() },
            assignedItems = assignedItems.map { assignedItem -> assignedItem.toUiModel() },
        )
}
