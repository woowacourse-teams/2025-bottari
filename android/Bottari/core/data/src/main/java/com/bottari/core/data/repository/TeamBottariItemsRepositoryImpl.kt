package com.bottari.core.data.repository

import com.bottari.core.data.source.remote.TeamBottariItemsRemoteDataSource
import com.bottari.core.domain.model.bottari.item.BottariItem
import com.bottari.core.domain.model.team.bottari.TeamBottariCheckList
import com.bottari.core.domain.model.team.bottari.TeamBottariStatus
import com.bottari.core.domain.model.team.bottari.item.TeamBottariItemType
import com.bottari.core.domain.repository.TeamBottariItemsRepository
import com.bottari.core.network.dto.team.bottari.item.AssignedItemsCreateRequest
import com.bottari.core.network.dto.team.bottari.item.AssignedItemsUpdateRequest
import com.bottari.core.network.dto.team.bottari.item.PersonalItemsCreateRequest
import com.bottari.core.network.dto.team.bottari.item.SharedItemsCreateRequest
import com.bottari.core.network.dto.team.bottari.item.TeamBottariItemCheckUpdateRequest
import com.bottari.core.network.dto.team.bottari.item.TeamBottariItemDeleteRequest
import com.bottari.core.network.dto.team.bottari.item.TeamBottariItemRemindRequest
import com.bottari.core.network.dto.team.bottari.item.TeamBottariItemUnCheckUpdateRequest
import javax.inject.Inject

class TeamBottariItemsRepositoryImpl @Inject constructor(
    val teamBottariRemoteDataSource: TeamBottariItemsRemoteDataSource,
) : TeamBottariItemsRepository {
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

    override suspend fun fetchTeamBottariStatus(id: Long): Result<TeamBottariStatus> =
        teamBottariRemoteDataSource
            .fetchTeamBottariStatus(id)
            .mapCatching { response -> response.toDomain() }

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

    override suspend fun sendRemindByItem(
        id: Long,
        type: String,
    ): Result<Unit> =
        teamBottariRemoteDataSource
            .sendRemindByItem(id, TeamBottariItemRemindRequest(type))
}
