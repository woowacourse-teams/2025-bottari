package com.bottari.core.data.source.remote

import com.bottari.core.data.common.safeApiCall
import com.bottari.core.network.dto.team.bottari.TeamBottariJoinRequest
import com.bottari.core.network.dto.team.member.TeamMemberFetchResponse
import com.bottari.core.network.dto.team.member.TeamMemberNameFetchResponse
import com.bottari.core.network.dto.team.member.TeamMemberStatusFetchResponse
import com.bottari.core.network.service.TeamMemberService
import javax.inject.Inject

class TeamMemberRemoteDataSourceImpl @Inject constructor(
    private val teamMemberService: TeamMemberService,
) : TeamMemberRemoteDataSource {
    override suspend fun fetchTeamMembers(id: Long): Result<TeamMemberFetchResponse> =
        safeApiCall { teamMemberService.fetchTeamMembers(id) }

    override suspend fun fetchTeamMembersStatus(id: Long): Result<List<TeamMemberStatusFetchResponse>> =
        safeApiCall { teamMemberService.fetchTeamMembersStatus(id) }

    override suspend fun sendRemindByMemberMessage(
        teamBottariId: Long,
        memberId: Long,
    ): Result<Unit> = safeApiCall { teamMemberService.sendRemindByMemberMessage(teamBottariId, memberId) }

    override suspend fun joinTeamBottari(request: TeamBottariJoinRequest): Result<Unit> =
        safeApiCall { teamMemberService.joinTeamBottari(request) }

    override suspend fun fetchTeamBottariMembers(teamBottariId: Long): Result<List<TeamMemberNameFetchResponse>> =
        safeApiCall { teamMemberService.fetchTeamBottariMembers(teamBottariId) }
}
