package com.bottari.data.source.remote

import com.bottari.data.common.util.safeApiCall
import com.bottari.data.model.team.TeamMembersResponse
import com.bottari.data.service.TeamBottariService

class TeamMembersRemoteDataSourceImpl(
    private val teamBottariService: TeamBottariService,
) : TeamMembersRemoteDataSource {
    override suspend fun fetchTeamMembers(id: Long): Result<TeamMembersResponse> = safeApiCall { teamBottariService.fetchTeamMembers(id) }
}
