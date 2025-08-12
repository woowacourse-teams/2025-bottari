package com.bottari.data.repository

import com.bottari.data.mapper.TeamBottariMapper.toDomain
import com.bottari.data.mapper.TeamMembersMapper.toDomain
import com.bottari.data.model.team.CreateTeamBottariRequest
import com.bottari.data.source.remote.TeamBottariRemoteDataSource
import com.bottari.domain.model.bottari.TeamBottari
import com.bottari.domain.model.team.TeamMembers
import com.bottari.domain.repository.TeamBottariRepository

class TeamBottariRepositoryImpl(
    private val teamBottariRemoteDataSource: TeamBottariRemoteDataSource,
) : TeamBottariRepository {
    override suspend fun createTeamBottari(title: String): Result<Long?> =
        teamBottariRemoteDataSource.createBottari(
            CreateTeamBottariRequest(title),
        )

    override suspend fun fetchTeamBottaries(): Result<List<TeamBottari>> =
        teamBottariRemoteDataSource
            .fetchTeamBottaries()
            .mapCatching { teamBottaries -> teamBottaries.map { it.toDomain() } }

    override suspend fun fetchTeamMembers(id: Long): Result<TeamMembers> =
        teamBottariRemoteDataSource
            .fetchTeamMembers(id)
            .mapCatching { response -> response.toDomain() }
}
