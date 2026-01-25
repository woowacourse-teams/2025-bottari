package com.bottari.core.ui.model.bottari.team.member

import android.os.Parcelable
import com.bottari.core.domain.model.team.member.TeamMember
import com.bottari.core.domain.model.team.member.TeamMemberStatus
import com.bottari.core.domain.model.team.member.TeamStatus
import kotlinx.parcelize.Parcelize

@Parcelize
data class TeamMemberUiModel(
    val id: Long?,
    val nickname: String,
    val isHost: Boolean,
) : Parcelable {
    companion object {
        fun fromDomain(teamMember: TeamMember): TeamMemberUiModel = TeamMemberUiModel(teamMember.memberId, teamMember.nickname, false)

        fun fromDomain(teamMemberStatus: TeamMemberStatus): TeamMemberUiModel =
            TeamMemberUiModel(
                teamMemberStatus.id,
                teamMemberStatus.nickname.value,
                teamMemberStatus.isHost,
            )

        fun fromDomain(teamStatus: TeamStatus): List<TeamMemberUiModel> =
            buildList {
                add(TeamMemberUiModel(null, teamStatus.hostName.value, true))
                teamStatus.nicknames
                    .forEach { nickname ->
                        if (nickname != teamStatus.hostName) {
                            add(TeamMemberUiModel(null, nickname.value, false))
                        }
                    }
            }
    }
}
