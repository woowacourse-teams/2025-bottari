package com.bottari.data.repository

import com.bottari.data.mapper.TeamBottariMapper.toDomain
import com.bottari.data.mapper.TeamMapper.toDomain
import com.bottari.data.mapper.TeamMembersMapper.toDomain
import com.bottari.data.model.team.CreateTeamBottariRequest
import com.bottari.data.model.team.ItemTypeRequest
import com.bottari.data.source.remote.TeamBottariRemoteDataSource
import com.bottari.domain.model.bottari.TeamBottari
import com.bottari.domain.model.team.TeamBottariCheckList
import com.bottari.domain.model.team.TeamBottariDetail
import com.bottari.domain.model.team.TeamBottariStatus
import com.bottari.domain.model.team.TeamMemberStatus
import com.bottari.domain.model.team.TeamMembers
import com.bottari.domain.repository.TeamBottariRepository

class TeamBottariRepositoryImpl(
    private val teamBottariRemoteDataSource: TeamBottariRemoteDataSource,
) : TeamBottariRepository {
    override suspend fun createTeamBottari(title: String): Result<Long?> =
        teamBottariRemoteDataSource.createBottari(
            CreateTeamBottariRequest(title),
        )

    override suspend fun fetchTeamBottari(teamBottariId: Long): Result<TeamBottariCheckList> =
        teamBottariRemoteDataSource.fetchTeamBottari(teamBottariId).mapCatching { it.toDomain() }

    override suspend fun uncheckBottariItem(
        bottariItemId: Long,
        type: String,
    ): Result<Unit> = teamBottariRemoteDataSource.uncheckBottariItem(bottariItemId, ItemTypeRequest(type))

    override suspend fun checkBottariItem(
        bottariItemId: Long,
        type: String,
    ): Result<Unit> = teamBottariRemoteDataSource.checkBottariItem(bottariItemId, ItemTypeRequest(type))

    override suspend fun fetchTeamBottaries(): Result<List<TeamBottari>> =
        teamBottariRemoteDataSource
            .fetchTeamBottaries()
            .mapCatching { teamBottaries -> teamBottaries.map { it.toDomain() } }

    override suspend fun fetchTeamMembers(id: Long): Result<TeamMembers> =
        teamBottariRemoteDataSource
            .fetchTeamMembers(id)
            .mapCatching { response -> response.toDomain() }

    override suspend fun fetchTeamBottariDetail(teamBottariId: Long): Result<TeamBottariDetail> =
        teamBottariRemoteDataSource
            .fetchTeamBottariDetail(teamBottariId)
            .mapCatching { response -> response.toDomain() }

    override suspend fun fetchTeamBottariStatus(id: Long): Result<TeamBottariStatus> =
        teamBottariRemoteDataSource
            .fetchTeamBottariStatus(id)
            .mapCatching { response -> response.toDomain() }

    override suspend fun remind(
        id: Long,
        type: String,
    ): Result<Unit> =
        teamBottariRemoteDataSource
            .remind(id, ItemTypeRequest(type))

    override suspend fun fetchTeamMembersStatus(id: Long): Result<List<TeamMemberStatus>> =
        teamBottariRemoteDataSource
            .fetchTeamMembersStatus(id)
            .mapCatching { responses -> responses.map { response -> response.toDomain() } }
}
