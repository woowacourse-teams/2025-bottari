package com.bottari.data.source.remote

import com.bottari.data.common.util.safeApiCall
import com.bottari.data.model.remote.team.bottari.FetchTeamBottariStatusResponse
import com.bottari.data.model.remote.team.bottari.item.request.AssignedItemsCreateRequest
import com.bottari.data.model.remote.team.bottari.item.request.AssignedItemsUpdateRequest
import com.bottari.data.model.remote.team.bottari.item.request.PersonalItemsCreateRequest
import com.bottari.data.model.remote.team.bottari.item.request.SharedItemsCreateRequest
import com.bottari.data.model.remote.team.bottari.item.request.TeamBottariItemCheckUpdateRequest
import com.bottari.data.model.remote.team.bottari.item.request.TeamBottariItemDeleteRequest
import com.bottari.data.model.remote.team.bottari.item.request.TeamBottariItemRemindRequest
import com.bottari.data.model.remote.team.bottari.item.request.TeamBottariItemUnCheckUpdateRequest
import com.bottari.data.model.remote.team.bottari.item.response.AssignedItemsFetchResponse
import com.bottari.data.model.remote.team.bottari.item.response.PersonalItemsFetchResponse
import com.bottari.data.model.remote.team.bottari.item.response.SharedItemsFetchResponse
import com.bottari.data.model.remote.team.bottari.item.response.TeamBottariItemChecklistFetchResponse
import com.bottari.data.service.TeamBottariItemsService

class TeamBottariItemsRemoteDataSourceImpl(
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
