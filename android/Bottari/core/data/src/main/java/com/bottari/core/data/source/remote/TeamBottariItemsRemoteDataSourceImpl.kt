package com.bottari.core.data.source.remote

import com.bottari.core.data.common.safeApiCall
import com.bottari.core.network.dto.team.bottari.FetchTeamBottariStatusResponse
import com.bottari.core.network.dto.team.bottari.item.AssignedItemsCreateRequest
import com.bottari.core.network.dto.team.bottari.item.AssignedItemsFetchResponse
import com.bottari.core.network.dto.team.bottari.item.AssignedItemsUpdateRequest
import com.bottari.core.network.dto.team.bottari.item.PersonalItemsCreateRequest
import com.bottari.core.network.dto.team.bottari.item.PersonalItemsFetchResponse
import com.bottari.core.network.dto.team.bottari.item.SharedItemsCreateRequest
import com.bottari.core.network.dto.team.bottari.item.SharedItemsFetchResponse
import com.bottari.core.network.dto.team.bottari.item.TeamBottariItemCheckUpdateRequest
import com.bottari.core.network.dto.team.bottari.item.TeamBottariItemChecklistFetchResponse
import com.bottari.core.network.dto.team.bottari.item.TeamBottariItemDeleteRequest
import com.bottari.core.network.dto.team.bottari.item.TeamBottariItemRemindRequest
import com.bottari.core.network.dto.team.bottari.item.TeamBottariItemUnCheckUpdateRequest
import com.bottari.core.network.service.TeamBottariItemsService
import javax.inject.Inject

class TeamBottariItemsRemoteDataSourceImpl @Inject constructor(
    val teamBottariItemsService: TeamBottariItemsService,
) : TeamBottariItemsRemoteDataSource {
    override suspend fun fetchTeamBottari(teamBottariId: Long): Result<TeamBottariItemChecklistFetchResponse> =
        safeApiCall {
            teamBottariItemsService.fetchTeamBottari(teamBottariId)
        }

    override suspend fun uncheckBottariItem(
        bottariItemId: Long,
        request: TeamBottariItemUnCheckUpdateRequest,
    ): Result<Unit> =
        safeApiCall {
            teamBottariItemsService.uncheckTeamBottariItem(bottariItemId, request)
        }

    override suspend fun checkBottariItem(
        bottariItemId: Long,
        request: TeamBottariItemCheckUpdateRequest,
    ): Result<Unit> =
        safeApiCall {
            teamBottariItemsService.checkTeamBottariItem(bottariItemId, request)
        }

    override suspend fun fetchTeamBottariStatus(id: Long): Result<FetchTeamBottariStatusResponse> =
        safeApiCall {
            teamBottariItemsService.fetchTeamBottariStatus(id)
        }

    override suspend fun sendRemindByItem(
        id: Long,
        type: TeamBottariItemRemindRequest,
    ): Result<Unit> =
        safeApiCall {
            teamBottariItemsService.sendRemindByItem(id, type)
        }

    override suspend fun createTeamBottariSharedItem(
        id: Long,
        request: SharedItemsCreateRequest,
    ): Result<Unit> =
        safeApiCall {
            teamBottariItemsService.createTeamBottariSharedItem(id, request)
        }

    override suspend fun createTeamBottariPersonalItem(
        id: Long,
        request: PersonalItemsCreateRequest,
    ): Result<Unit> =
        safeApiCall {
            teamBottariItemsService.createTeamBottariPersonalItem(id, request)
        }

    override suspend fun createTeamBottariAssignedItem(
        id: Long,
        request: AssignedItemsCreateRequest,
    ): Result<Unit> =
        safeApiCall {
            teamBottariItemsService.createTeamBottariAssignedItem(id, request)
        }

    override suspend fun deleteTeamBottariItem(
        id: Long,
        type: TeamBottariItemDeleteRequest,
    ): Result<Unit> =
        safeApiCall {
            teamBottariItemsService.deleteTeamBottariItem(id, type)
        }

    override suspend fun fetchTeamAssignedItems(teamBottariId: Long): Result<List<AssignedItemsFetchResponse>> =
        safeApiCall { teamBottariItemsService.fetchTeamAssignedItems(teamBottariId) }

    override suspend fun fetchTeamSharedItems(teamBottariId: Long): Result<List<SharedItemsFetchResponse>> =
        safeApiCall { teamBottariItemsService.fetchTeamSharedItems(teamBottariId) }

    override suspend fun fetchTeamPersonalItems(teamBottariId: Long): Result<List<PersonalItemsFetchResponse>> =
        safeApiCall { teamBottariItemsService.fetchTeamPersonalItems(teamBottariId) }

    override suspend fun saveTeamBottariAssignedItem(
        teamBottariId: Long,
        assignedItemId: Long,
        request: AssignedItemsUpdateRequest,
    ): Result<Unit> =
        safeApiCall {
            teamBottariItemsService.saveTeamAssignedItem(teamBottariId, assignedItemId, request)
        }
}
