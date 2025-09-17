package com.bottari.data.repository

import com.bottari.data.model.remote.team.bottari.TeamBottariJoinRequest
import com.bottari.data.model.remote.team.bottari.item.request.AssignedItemsCreateRequest
import com.bottari.data.model.remote.team.bottari.item.request.AssignedItemsUpdateRequest
import com.bottari.data.model.remote.team.bottari.item.request.PersonalItemsCreateRequest
import com.bottari.data.model.remote.team.bottari.item.request.SharedItemsCreateRequest
import com.bottari.data.model.remote.team.bottari.item.request.TeamBottariItemCheckUpdateRequest
import com.bottari.data.model.remote.team.bottari.item.request.TeamBottariItemDeleteRequest
import com.bottari.data.model.remote.team.bottari.item.request.TeamBottariItemRemindRequest
import com.bottari.data.model.remote.team.bottari.item.request.TeamBottariItemUnCheckUpdateRequest
import com.bottari.data.source.remote.TeamBottariRemoteDataSource
import com.bottari.domain.model.bottari.item.BottariItem
import com.bottari.domain.model.team.bottari.TeamBottari
import com.bottari.domain.model.team.bottari.TeamBottariCheckList
import com.bottari.domain.model.team.bottari.TeamBottariDetail
import com.bottari.domain.model.team.bottari.TeamBottariStatus
import com.bottari.domain.model.team.bottari.item.TeamBottariItemType
import com.bottari.domain.model.team.member.TeamMember
import com.bottari.domain.model.team.member.TeamMemberStatus
import com.bottari.domain.model.team.member.TeamStatus
import com.bottari.domain.repository.TeamBottariRepository

class TeamBottariRepositoryImpl(
    private val teamBottariRemoteDataSource: TeamBottariRemoteDataSource,
) : TeamBottariRepository {
    override suspend fun createTeamBottari(title: String): Result<Long?> =
        teamBottariRemoteDataSource.createBottari(
            com.bottari.data.model.remote.team.bottari
                .TeamBottariCreateRequest(title),
        )

    override suspend fun fetchTeamBottari(teamBottariId: Long): Result<TeamBottariCheckList> =
        teamBottariRemoteDataSource.fetchTeamBottari(teamBottariId).mapCatching { it.toDomain() }

    override suspend fun uncheckBottariItem(
        bottariItemId: Long,
        type: String,
    ): Result<Unit> =
        teamBottariRemoteDataSource.uncheckBottariItem(
            bottariItemId,
            TeamBottariItemUnCheckUpdateRequest(type),
        )

    override suspend fun checkBottariItem(
        bottariItemId: Long,
        type: String,
    ): Result<Unit> =
        teamBottariRemoteDataSource.checkBottariItem(
            bottariItemId,
            TeamBottariItemCheckUpdateRequest(type),
        )

    override suspend fun fetchTeamBottaries(): Result<List<TeamBottari>> =
        teamBottariRemoteDataSource
            .fetchTeamBottaries()
            .mapCatching { teamBottaries -> teamBottaries.map { it.toDomain() } }

    override suspend fun fetchTeamMembers(id: Long): Result<TeamStatus> =
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

    override suspend fun sendRemindByItem(
        id: Long,
        type: String,
    ): Result<Unit> =
        teamBottariRemoteDataSource
            .sendRemindByItem(id, TeamBottariItemRemindRequest(type))

    override suspend fun fetchTeamMembersStatus(id: Long): Result<List<TeamMemberStatus>> =
        teamBottariRemoteDataSource
            .fetchTeamMembersStatus(id)
            .mapCatching { responses -> responses.map { response -> response.toDomain() } }

    override suspend fun createTeamBottariSharedItem(
        id: Long,
        name: String,
    ): Result<Unit> =
        teamBottariRemoteDataSource.createTeamBottariSharedItem(
            id,
            SharedItemsCreateRequest(name),
        )

    override suspend fun createTeamBottariPersonalItem(
        id: Long,
        name: String,
    ): Result<Unit> =
        teamBottariRemoteDataSource.createTeamBottariPersonalItem(
            id,
            PersonalItemsCreateRequest(name),
        )

    override suspend fun createTeamBottariAssignedItem(
        id: Long,
        name: String,
        teamMemberIds: List<Long>,
    ): Result<Unit> =
        teamBottariRemoteDataSource.createTeamBottariAssignedItem(
            id,
            AssignedItemsCreateRequest(name, teamMemberIds),
        )

    override suspend fun deleteTeamBottariItem(
        id: Long,
        type: TeamBottariItemType,
    ): Result<Unit> {
        val bottariItemType = type.javaClass.simpleName
        return teamBottariRemoteDataSource.deleteTeamBottariItem(
            id,
            TeamBottariItemDeleteRequest(bottariItemType),
        )
    }

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

    override suspend fun fetchTeamAssignedItems(teamBottariId: Long): Result<List<BottariItem>> =
        teamBottariRemoteDataSource
            .fetchTeamAssignedItems(teamBottariId)
            .mapCatching { assignedItems -> assignedItems.map { assignedItem -> assignedItem.toDomain() } }

    override suspend fun fetchTeamSharedItems(teamBottariId: Long): Result<List<BottariItem>> =
        teamBottariRemoteDataSource
            .fetchTeamSharedItems(teamBottariId)
            .mapCatching { sharedItems -> sharedItems.map { sharedItem -> sharedItem.toDomain() } }

    override suspend fun fetchTeamPersonalItems(teamBottariId: Long): Result<List<BottariItem>> =
        teamBottariRemoteDataSource
            .fetchTeamPersonalItems(teamBottariId)
            .mapCatching { personalItems -> personalItems.map { personalItem -> personalItem.toDomain() } }

    override suspend fun saveTeamBottariAssignedItem(
        teamBottariId: Long,
        assignedItemId: Long,
        name: String,
        assigneeIds: List<Long>,
    ): Result<Unit> =
        teamBottariRemoteDataSource.saveTeamBottariAssignedItem(
            teamBottariId,
            assignedItemId,
            AssignedItemsUpdateRequest(name, assigneeIds),
        )

    override suspend fun exitTeamBottari(teamBottariId: Long): Result<Unit> = teamBottariRemoteDataSource.exitTeamBottari(teamBottariId)
}
