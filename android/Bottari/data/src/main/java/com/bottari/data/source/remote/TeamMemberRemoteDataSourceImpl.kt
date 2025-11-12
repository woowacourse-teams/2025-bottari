package com.bottari.data.source.remote

import com.bottari.data.common.util.safeApiCall
import com.bottari.data.model.remote.team.bottari.TeamBottariJoinRequest
import com.bottari.data.model.remote.team.member.TeamMemberFetchResponse
import com.bottari.data.model.remote.team.member.TeamMemberNameFetchResponse
import com.bottari.data.model.remote.team.member.TeamMemberStatusFetchResponse
import com.bottari.data.service.TeamMemberService
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
