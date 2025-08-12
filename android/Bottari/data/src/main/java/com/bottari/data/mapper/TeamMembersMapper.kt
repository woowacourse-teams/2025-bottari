package com.bottari.data.mapper

import com.bottari.data.model.team.TeamMembersResponse
import com.bottari.domain.model.member.Nickname
import com.bottari.domain.model.team.HeadCount
import com.bottari.domain.model.team.TeamMembers

object TeamMembersMapper {
    fun TeamMembersResponse.toDomain(): TeamMembers =
        TeamMembers(
            inviteCode = inviteCode,
            teamMemberHeadCount = HeadCount(teamMemberCount),
            hostName = Nickname(ownerName),
            memberNicknames = teamMemberNames.map { name -> Nickname(name) },
        )
}
