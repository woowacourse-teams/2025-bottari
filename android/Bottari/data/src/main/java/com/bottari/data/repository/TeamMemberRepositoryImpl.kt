package com.bottari.data.repository

import com.bottari.data.model.remote.team.bottari.TeamBottariJoinRequest
import com.bottari.data.source.remote.TeamMemberRemoteDataSource
import com.bottari.domain.model.team.member.TeamMember
import com.bottari.domain.model.team.member.TeamMemberStatus
import com.bottari.domain.model.team.member.TeamStatus
import com.bottari.domain.repository.TeamMemberRepository

class TeamMemberRepositoryImpl(
    val teamBottariRemoteDataSource: TeamMemberRemoteDataSource,
) : TeamMemberRepository {
    override suspend fun fetchTeamMembers(id: Long): Result<TeamStatus> =
        teamBottariRemoteDataSource
            .fetchTeamMembers(id)
            .mapCatching { response -> response.toDomain() }

    override suspend fun fetchTeamMembersStatus(id: Long): Result<List<TeamMemberStatus>> =
        teamBottariRemoteDataSource
            .fetchTeamMembersStatus(id)
            .mapCatching { responses -> responses.map { response -> response.toDomain() } }

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
            .mapCatching { members ->
                members.map { member -> member.toDomain() }
            }
}
