package com.bottari.data.mapper.team.member

import com.bottari.data.mapper.bottari.item.BottariItemMapper.toDomain
import com.bottari.data.model.team.bottari.TeamMemberStatusCheckedFetchResponse
import com.bottari.data.model.team.bottari.item.response.AssignedItemsFetchResponse
import com.bottari.data.model.team.member.TeamMemberFetchResponse
import com.bottari.data.model.team.member.TeamMemberNameFetchResponse
import com.bottari.data.model.team.member.TeamMemberStatusFetchResponse
import com.bottari.domain.model.bottari.item.BottariItemCount
import com.bottari.domain.model.member.Nickname
import com.bottari.domain.model.team.member.HeadCount
import com.bottari.domain.model.team.member.MemberCheckStatus
import com.bottari.domain.model.team.member.TeamMember
import com.bottari.domain.model.team.member.TeamMemberStatus
import com.bottari.domain.model.team.member.TeamMembers

object TeamMembersMapper {
    fun TeamMemberFetchResponse.toDomain(): TeamMembers =
        TeamMembers(
            inviteCode = inviteCode,
            teamMemberHeadCount = HeadCount(teamMemberCount),
            hostName = Nickname(ownerName),
            memberNicknames = teamMemberNames.map { name -> Nickname(name) },
        )

    fun TeamMemberStatusFetchResponse.toDomain(): TeamMemberStatus =
        TeamMemberStatus(
            id = id,
            nickname = Nickname(nickname),
            isHost = isOwner,
            itemCount =
                BottariItemCount(
                    totalQuantity = totalItemsCount,
                    checkedQuantity = checkedItemsCount,
                ),
            sharedItems = sharedItems.map { sharedItem -> sharedItem.toDomain() },
            assignedItems = assignedItems.map { assignedItem -> assignedItem.toDomain() },
        )

    fun TeamMemberNameFetchResponse.toDomain(): TeamMember =
        TeamMember(
            memberId = id,
            nickname = name,
        )

    fun AssignedItemsFetchResponse.Assignee.toDomain() =
        TeamMember(
            memberId = memberId,
            nickname = name,
        )

    fun TeamMemberStatusCheckedFetchResponse.toDomain(): MemberCheckStatus =
        MemberCheckStatus(
            name = name,
            checked = checked,
        )
}
