package com.bottari.data.repository

import com.bottari.data.source.remote.TeamBottariRemoteDataSource
import com.bottari.domain.model.team.bottari.TeamBottari
import com.bottari.domain.model.team.bottari.TeamBottariDetail
import com.bottari.domain.repository.TeamBottariRepository

class TeamBottariRepositoryImpl(
    private val teamBottariRemoteDataSource: TeamBottariRemoteDataSource,
) : TeamBottariRepository {
    override suspend fun createTeamBottari(title: String): Result<Long?> =
        teamBottariRemoteDataSource.createBottari(
            com.bottari.data.model.remote.team.bottari
                .TeamBottariCreateRequest(title),
        )

    override suspend fun fetchTeamBottaries(): Result<List<TeamBottari>> =
        teamBottariRemoteDataSource
            .fetchTeamBottaries()
            .mapCatching { teamBottaries -> teamBottaries.map { it.toDomain() } }

    override suspend fun fetchTeamBottariDetail(teamBottariId: Long): Result<TeamBottariDetail> =
        teamBottariRemoteDataSource
            .fetchTeamBottariDetail(teamBottariId)
            .mapCatching { response -> response.toDomain() }

    override suspend fun exitTeamBottari(teamBottariId: Long): Result<Unit> = teamBottariRemoteDataSource.exitTeamBottari(teamBottariId)
}
