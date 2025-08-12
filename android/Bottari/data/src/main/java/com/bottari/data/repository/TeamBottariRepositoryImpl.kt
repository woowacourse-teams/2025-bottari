package com.bottari.data.repository

import com.bottari.data.model.team.CreateTeamBottariRequest
import com.bottari.data.source.remote.TeamBottariRemoteDataSource
import com.bottari.domain.repository.TeamBottariRepository

class TeamBottariRepositoryImpl(
    private val teamBottariRemoteDataSource: TeamBottariRemoteDataSource,
) : TeamBottariRepository {
    override suspend fun createTeamBottari(title: String): Result<Long?> =
        teamBottariRemoteDataSource.createBottari(
            CreateTeamBottariRequest(title),
        )
}
