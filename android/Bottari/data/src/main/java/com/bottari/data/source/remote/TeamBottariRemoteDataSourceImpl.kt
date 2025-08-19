package com.bottari.data.source.remote

import com.bottari.data.common.extension.extractIdFromHeader
import com.bottari.data.common.util.safeApiCall
import com.bottari.data.model.common.ErrorResponse
import com.bottari.data.model.team.CreateTeamBottariAssignedItemRequest
import com.bottari.data.model.team.CreateTeamBottariPersonalItemRequest
import com.bottari.data.model.team.CreateTeamBottariRequest
import com.bottari.data.model.team.CreateTeamBottariSharedItemRequest
import com.bottari.data.model.team.DeleteTeamBottariItemRequest
import com.bottari.data.model.team.FetchTeamBottariChecklistResponse
import com.bottari.data.model.team.FetchTeamBottariDetailResponse
import com.bottari.data.model.team.FetchTeamBottariMemberResponse
import com.bottari.data.model.team.FetchTeamBottariResponse
import com.bottari.data.model.team.FetchTeamBottariStatusResponse
import com.bottari.data.model.team.FetchTeamMemberStatusResponse
import com.bottari.data.model.team.FetchTeamMembersResponse
import com.bottari.data.model.team.ItemTypeRequest
import com.bottari.data.model.team.JoinTeamBottariRequest
import com.bottari.data.model.team.SaveTeamBottariAssignedItemRequest
import com.bottari.data.model.teamItem.FetchTeamAssignedItemResponse
import com.bottari.data.model.teamItem.FetchTeamPersonalItemResponse
import com.bottari.data.model.teamItem.FetchTeamSharedItemResponse
import com.bottari.data.service.TeamBottariService

class TeamBottariRemoteDataSourceImpl(
    private val teamBottariService: TeamBottariService,
) : TeamBottariRemoteDataSource {
    override suspend fun createBottari(request: CreateTeamBottariRequest): Result<Long?> =
        runCatching {
            val response = teamBottariService.createTeamBottari(request)
            if (response.isSuccessful) {
                return Result.success(response.extractIdFromHeader(HEADER_TEAM_BOTTARI_ID_PREFIX))
            }

            val errorResponse = ErrorResponse.parseErrorResponse(response.errorBody())
            return Result.failure(Exception(errorResponse?.title))
        }

    override suspend fun fetchTeamBottari(teamBottariId: Long): Result<FetchTeamBottariChecklistResponse> =
        safeApiCall {
            teamBottariService.fetchTeamBottari(teamBottariId)
        }

    override suspend fun uncheckBottariItem(
        bottariItemId: Long,
        request: ItemTypeRequest,
    ): Result<Unit> =
        safeApiCall {
            teamBottariService.uncheckTeamBottariItem(bottariItemId, request)
        }

    override suspend fun checkBottariItem(
        bottariItemId: Long,
        request: ItemTypeRequest,
    ): Result<Unit> =
        safeApiCall {
            teamBottariService.checkTeamBottariItem(bottariItemId, request)
        }

    override suspend fun fetchTeamBottaries(): Result<List<FetchTeamBottariResponse>> =
        safeApiCall {
            teamBottariService.fetchTeamBottaries()
        }

    override suspend fun fetchTeamBottariDetail(teamBottariId: Long): Result<FetchTeamBottariDetailResponse> =
        safeApiCall {
            teamBottariService.fetchTeamBottariDetail(teamBottariId)
        }

    override suspend fun fetchTeamBottariStatus(id: Long): Result<FetchTeamBottariStatusResponse> =
        safeApiCall {
            teamBottariService.fetchTeamBottariStatus(id)
        }

    override suspend fun sendRemindByItem(
        id: Long,
        type: ItemTypeRequest,
    ): Result<Unit> =
        safeApiCall {
            teamBottariService.sendRemindByItem(id, type)
        }

    override suspend fun fetchTeamMembers(id: Long): Result<FetchTeamMembersResponse> =
        safeApiCall { teamBottariService.fetchTeamMembers(id) }

    override suspend fun fetchTeamMembersStatus(id: Long): Result<List<FetchTeamMemberStatusResponse>> =
        safeApiCall { teamBottariService.fetchTeamMembersStatus(id) }

    override suspend fun createTeamBottariSharedItem(
        id: Long,
        request: CreateTeamBottariSharedItemRequest,
    ): Result<Unit> =
        safeApiCall {
            teamBottariService.createTeamBottariSharedItem(id, request)
        }

    override suspend fun createTeamBottariPersonalItem(
        id: Long,
        request: CreateTeamBottariPersonalItemRequest,
    ): Result<Unit> =
        safeApiCall {
            teamBottariService.createTeamBottariPersonalItem(id, request)
        }

    override suspend fun createTeamBottariAssignedItem(
        id: Long,
        request: CreateTeamBottariAssignedItemRequest,
    ): Result<Unit> =
        safeApiCall {
            teamBottariService.createTeamBottariAssignedItem(id, request)
        }

    override suspend fun deleteTeamBottariItem(
        id: Long,
        type: DeleteTeamBottariItemRequest,
    ): Result<Unit> =
        safeApiCall {
            teamBottariService.deleteTeamBottariItem(id, type)
        }

    override suspend fun sendRemindByMemberMessage(
        teamBottariId: Long,
        memberId: Long,
    ): Result<Unit> = safeApiCall { teamBottariService.sendRemindByMemberMessage(teamBottariId, memberId) }

    override suspend fun joinTeamBottari(request: JoinTeamBottariRequest): Result<Unit> =
        safeApiCall { teamBottariService.joinTeamBottari(request) }

    override suspend fun fetchTeamBottariMembers(teamBottariId: Long): Result<List<FetchTeamBottariMemberResponse>> =
        safeApiCall { teamBottariService.fetchTeamBottariMembers(teamBottariId) }

    override suspend fun fetchTeamAssignedItems(teamBottariId: Long): Result<List<FetchTeamAssignedItemResponse>> =
        safeApiCall { teamBottariService.fetchTeamAssignedItems(teamBottariId) }

    override suspend fun fetchTeamSharedItems(teamBottariId: Long): Result<List<FetchTeamSharedItemResponse>> =
        safeApiCall { teamBottariService.fetchTeamSharedItems(teamBottariId) }

    override suspend fun fetchTeamPersonalItems(teamBottariId: Long): Result<List<FetchTeamPersonalItemResponse>> =
        safeApiCall { teamBottariService.fetchTeamPersonalItems(teamBottariId) }

    override suspend fun saveTeamBottariAssignedItem(
        teamBottariId: Long,
        assignedItemId: Long,
        request: SaveTeamBottariAssignedItemRequest,
    ): Result<Unit> =
        safeApiCall {
            teamBottariService.saveTeamAssignedItem(teamBottariId, assignedItemId, request)
        }

    companion object {
        private const val HEADER_TEAM_BOTTARI_ID_PREFIX = "/team-bottaries/"
    }
}
