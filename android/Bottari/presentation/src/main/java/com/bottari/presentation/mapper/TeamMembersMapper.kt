package com.bottari.presentation.mapper

import com.bottari.domain.model.team.TeamMembers
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
}
