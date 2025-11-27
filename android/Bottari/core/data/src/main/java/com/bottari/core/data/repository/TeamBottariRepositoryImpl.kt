package com.bottari.core.data.repository

import com.bottari.core.data.source.remote.TeamBottariRemoteDataSource
import com.bottari.core.domain.model.team.bottari.TeamBottari
import com.bottari.core.domain.model.team.bottari.TeamBottariDetail
import com.bottari.core.domain.repository.TeamBottariRepository
import com.bottari.core.network.dto.team.bottari.TeamBottariCreateRequest
import com.bottari.core.network.dto.team.bottari.TeamBottariDetailFetchResponse
import com.bottari.core.network.dto.team.bottari.TeamBottariFetchResponse
import javax.inject.Inject

class TeamBottariRepositoryImpl @Inject constructor(
    private val teamBottariRemoteDataSource: TeamBottariRemoteDataSource,
) : TeamBottariRepository {
    override suspend fun createTeamBottari(title: String): Result<Long?> =
        teamBottariRemoteDataSource.createBottari(TeamBottariCreateRequest(title))

    override suspend fun fetchTeamBottaries(): Result<List<TeamBottari>> =
        teamBottariRemoteDataSource
            .fetchTeamBottaries()
            .mapCatching { teamBottaries -> teamBottaries.map(TeamBottariFetchResponse::toDomain) }

    override suspend fun fetchTeamBottariDetail(teamBottariId: Long): Result<TeamBottariDetail> =
        teamBottariRemoteDataSource
            .fetchTeamBottariDetail(teamBottariId)
            .mapCatching(TeamBottariDetailFetchResponse::toDomain)

    override suspend fun exitTeamBottari(teamBottariId: Long): Result<Unit> = teamBottariRemoteDataSource.exitTeamBottari(teamBottariId)
}
