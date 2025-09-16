package com.bottari.presentation.model

import android.os.Parcelable
import com.bottari.domain.model.team.member.TeamMember
import com.bottari.domain.model.team.member.TeamMembers
import kotlinx.parcelize.Parcelize

@Parcelize
data class TeamMemberUiModel(
    val id: Long?,
    val nickname: String,
    val isHost: Boolean,
) : Parcelable {
    companion object {
        fun fromDomain(teamMember: TeamMember): TeamMemberUiModel = TeamMemberUiModel(teamMember.memberId, teamMember.nickname, false)

        fun fromDomain(teamMembers: TeamMembers): List<TeamMemberUiModel> =
            buildList {
                add(TeamMemberUiModel(null, teamMembers.hostName.value, true))
                teamMembers.memberNicknames
                    .forEach { nickname ->
                        if (nickname != teamMembers.hostName) add(TeamMemberUiModel(null, nickname.value, false))
                    }
            }
    }
}
