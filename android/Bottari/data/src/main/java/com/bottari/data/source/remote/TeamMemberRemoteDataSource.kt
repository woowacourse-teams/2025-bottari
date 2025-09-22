package com.bottari.data.source.remote

import com.bottari.data.model.remote.team.bottari.TeamBottariJoinRequest
import com.bottari.data.model.remote.team.member.TeamMemberFetchResponse
import com.bottari.data.model.remote.team.member.TeamMemberNameFetchResponse
import com.bottari.data.model.remote.team.member.TeamMemberStatusFetchResponse

interface TeamMemberRemoteDataSource {
    suspend fun fetchTeamMembers(id: Long): Result<TeamMemberFetchResponse>

    suspend fun fetchTeamMembersStatus(id: Long): Result<List<TeamMemberStatusFetchResponse>>

    suspend fun sendRemindByMemberMessage(
        teamBottariId: Long,
        memberId: Long,
    ): Result<Unit>

    suspend fun joinTeamBottari(request: TeamBottariJoinRequest): Result<Unit>

    suspend fun fetchTeamBottariMembers(teamBottariId: Long): Result<List<TeamMemberNameFetchResponse>>
}
