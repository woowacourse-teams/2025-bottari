package com.bottari.data.source.remote

import com.bottari.data.model.team.TeamMembersResponse

interface TeamMembersRemoteDataSource {
    suspend fun fetchTeamMembers(id: Long): Result<TeamMembersResponse>
}
