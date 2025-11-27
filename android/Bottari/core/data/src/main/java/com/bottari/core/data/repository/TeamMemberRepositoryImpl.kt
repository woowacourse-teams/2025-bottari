package com.bottari.core.data.repository

import com.bottari.core.data.source.remote.TeamMemberRemoteDataSource
import com.bottari.core.domain.model.team.member.TeamMember
import com.bottari.core.domain.model.team.member.TeamMemberStatus
import com.bottari.core.domain.model.team.member.TeamStatus
import com.bottari.core.domain.repository.TeamMemberRepository
import com.bottari.core.network.dto.team.bottari.TeamBottariJoinRequest
import com.bottari.core.network.dto.team.member.TeamMemberFetchResponse
import com.bottari.core.network.dto.team.member.TeamMemberNameFetchResponse
import com.bottari.core.network.dto.team.member.TeamMemberStatusFetchResponse
import javax.inject.Inject

class TeamMemberRepositoryImpl @Inject constructor(
    val teamBottariRemoteDataSource: TeamMemberRemoteDataSource,
) : TeamMemberRepository {
    override suspend fun fetchTeamMembers(id: Long): Result<TeamStatus> =
        teamBottariRemoteDataSource
            .fetchTeamMembers(id)
            .mapCatching(TeamMemberFetchResponse::toDomain)

    override suspend fun fetchTeamMembersStatus(id: Long): Result<List<TeamMemberStatus>> =
        teamBottariRemoteDataSource
            .fetchTeamMembersStatus(id)
            .mapCatching { response -> response.map(TeamMemberStatusFetchResponse::toDomain) }

    override suspend fun sendRemindByMemberMessage(
        teamBottariId: Long,
        memberId: Long,
    ): Result<Unit> =
        teamBottariRemoteDataSource.sendRemindByMemberMessage(
            teamBottariId,
            memberId,
        )

    override suspend fun joinTeamBottari(inviteCode: String): Result<Unit> =
        teamBottariRemoteDataSource.joinTeamBottari(TeamBottariJoinRequest(inviteCode))

    override suspend fun fetchTeamBottariMembers(teamBottariId: Long): Result<List<TeamMember>> =
        teamBottariRemoteDataSource
            .fetchTeamBottariMembers(teamBottariId)
            .mapCatching { members -> members.map(TeamMemberNameFetchResponse::toDomain) }
}
