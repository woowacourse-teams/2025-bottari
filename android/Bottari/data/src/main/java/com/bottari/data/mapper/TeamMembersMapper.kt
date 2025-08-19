package com.bottari.data.mapper

import com.bottari.data.mapper.BottariItemMapper.toDomain
import com.bottari.data.model.team.FetchTeamBottariMemberResponse
import com.bottari.data.model.team.FetchTeamMemberStatusResponse
import com.bottari.data.model.team.FetchTeamMembersResponse
import com.bottari.domain.model.member.Nickname
import com.bottari.domain.model.team.HeadCount
import com.bottari.domain.model.team.TeamMember
import com.bottari.domain.model.team.TeamMemberStatus
import com.bottari.domain.model.team.TeamMembers

object TeamMembersMapper {
    fun FetchTeamMembersResponse.toDomain(): TeamMembers =
        TeamMembers(
            inviteCode = inviteCode,
            teamMemberHeadCount = HeadCount(teamMemberCount),
            hostName = Nickname(ownerName),
            memberNicknames = teamMemberNames.map { name -> Nickname(name) },
        )

    fun FetchTeamMemberStatusResponse.toDomain(): TeamMemberStatus =
        TeamMemberStatus(
            id = id,
            nickname = Nickname(nickname),
            isHost = isOwner,
            totalItemsCount = totalItemsCount,
            checkedItemsCount = checkedItemsCount,
            sharedItems = sharedItems.map { sharedItem -> sharedItem.toDomain() },
            assignedItems = assignedItems.map { assignedItem -> assignedItem.toDomain() },
        )

    fun FetchTeamBottariMemberResponse.toDomain(): TeamMember =
        TeamMember(
            memberId = id,
            nickname = name,
        )
}
