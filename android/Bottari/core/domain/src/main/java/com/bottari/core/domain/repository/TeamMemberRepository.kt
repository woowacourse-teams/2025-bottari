package com.bottari.core.domain.repository

import com.bottari.core.domain.model.team.member.TeamMember
import com.bottari.core.domain.model.team.member.TeamMemberStatus
import com.bottari.core.domain.model.team.member.TeamStatus

interface TeamMemberRepository {
    suspend fun fetchTeamMembers(id: Long): Result<TeamStatus>

    suspend fun fetchTeamMembersStatus(id: Long): Result<List<TeamMemberStatus>>

    suspend fun sendRemindByMemberMessage(
        teamBottariId: Long,
        memberId: Long,
    ): Result<Unit>

    suspend fun joinTeamBottari(inviteCode: String): Result<Unit>

    suspend fun fetchTeamBottariMembers(teamBottariId: Long): Result<List<TeamMember>>
}
