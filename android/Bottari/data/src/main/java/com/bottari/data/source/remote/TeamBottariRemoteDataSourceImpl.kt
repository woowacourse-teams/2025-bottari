package com.bottari.data.source.remote

import com.bottari.data.common.extension.extractIdFromHeader
import com.bottari.data.common.util.safeApiCall
import com.bottari.data.model.common.ErrorResponse
import com.bottari.data.model.team.bottari.FetchTeamBottariStatusResponse
import com.bottari.data.model.team.bottari.TeamBottariCreateRequest
import com.bottari.data.model.team.bottari.TeamBottariFetchDetailResponse
import com.bottari.data.model.team.bottari.TeamBottariFetchResponse
import com.bottari.data.model.team.bottari.TeamBottariJoinRequest
import com.bottari.data.model.team.bottari.item.AssignedItemsCreateRequest
import com.bottari.data.model.team.bottari.item.AssignedItemsFetchResponse
import com.bottari.data.model.team.bottari.item.AssignedItemsUpdateRequest
import com.bottari.data.model.team.bottari.item.PersonalItemsCreateRequest
import com.bottari.data.model.team.bottari.item.PersonalItemsFetchResponse
import com.bottari.data.model.team.bottari.item.SharedItemsCreateRequest
import com.bottari.data.model.team.bottari.item.SharedItemsFetchResponse
import com.bottari.data.model.team.bottari.item.TeamBottariItemCheckUpdateRequest
import com.bottari.data.model.team.bottari.item.TeamBottariItemChecklistFetchResponse
import com.bottari.data.model.team.bottari.item.TeamBottariItemDeleteRequest
import com.bottari.data.model.team.bottari.item.TeamBottariItemRemindRequest
import com.bottari.data.model.team.bottari.item.TeamBottariItemUnCheckUpdateRequest
import com.bottari.data.model.team.member.TeamMemberFetchResponse
import com.bottari.data.model.team.member.TeamMemberNameFetchResponse
import com.bottari.data.model.team.member.TeamMemberStatusFetchResponse
import com.bottari.data.service.TeamBottariService

class TeamBottariRemoteDataSourceImpl(
    private val teamBottariService: TeamBottariService,
) : TeamBottariRemoteDataSource {
    override suspend fun createBottari(request: TeamBottariCreateRequest): Result<Long?> =
        runCatching {
            val response = teamBottariService.createTeamBottari(request)
            if (response.isSuccessful) {
                return Result.success(response.extractIdFromHeader(HEADER_TEAM_BOTTARI_ID_PREFIX))
            }

            val errorResponse = ErrorResponse.parseErrorResponse(response.errorBody())
            return Result.failure(Exception(errorResponse?.title))
        }

    override suspend fun fetchTeamBottari(teamBottariId: Long): Result<TeamBottariItemChecklistFetchResponse> =
        safeApiCall {
            teamBottariService.fetchTeamBottari(teamBottariId)
        }

    override suspend fun uncheckBottariItem(
        bottariItemId: Long,
        request: TeamBottariItemUnCheckUpdateRequest,
    ): Result<Unit> =
        safeApiCall {
            teamBottariService.uncheckTeamBottariItem(bottariItemId, request)
        }

    override suspend fun checkBottariItem(
        bottariItemId: Long,
        request: TeamBottariItemCheckUpdateRequest,
    ): Result<Unit> =
        safeApiCall {
            teamBottariService.checkTeamBottariItem(bottariItemId, request)
        }

    override suspend fun fetchTeamBottaries(): Result<List<TeamBottariFetchResponse>> =
        safeApiCall {
            teamBottariService.fetchTeamBottaries()
        }

    override suspend fun fetchTeamBottariDetail(teamBottariId: Long): Result<TeamBottariFetchDetailResponse> =
        safeApiCall {
            teamBottariService.fetchTeamBottariDetail(teamBottariId)
        }

    override suspend fun fetchTeamBottariStatus(id: Long): Result<FetchTeamBottariStatusResponse> =
        safeApiCall {
            teamBottariService.fetchTeamBottariStatus(id)
        }

    override suspend fun sendRemindByItem(
        id: Long,
        type: TeamBottariItemRemindRequest,
    ): Result<Unit> =
        safeApiCall {
            teamBottariService.sendRemindByItem(id, type)
        }

    override suspend fun fetchTeamMembers(id: Long): Result<TeamMemberFetchResponse> =
        safeApiCall { teamBottariService.fetchTeamMembers(id) }

    override suspend fun fetchTeamMembersStatus(id: Long): Result<List<TeamMemberStatusFetchResponse>> =
        safeApiCall { teamBottariService.fetchTeamMembersStatus(id) }

    override suspend fun createTeamBottariSharedItem(
        id: Long,
        request: SharedItemsCreateRequest,
    ): Result<Unit> =
        safeApiCall {
            teamBottariService.createTeamBottariSharedItem(id, request)
        }

    override suspend fun createTeamBottariPersonalItem(
        id: Long,
        request: PersonalItemsCreateRequest,
    ): Result<Unit> =
        safeApiCall {
            teamBottariService.createTeamBottariPersonalItem(id, request)
        }

    override suspend fun createTeamBottariAssignedItem(
        id: Long,
        request: AssignedItemsCreateRequest,
    ): Result<Unit> =
        safeApiCall {
            teamBottariService.createTeamBottariAssignedItem(id, request)
        }

    override suspend fun deleteTeamBottariItem(
        id: Long,
        type: TeamBottariItemDeleteRequest,
    ): Result<Unit> =
        safeApiCall {
            teamBottariService.deleteTeamBottariItem(id, type)
        }

    override suspend fun sendRemindByMemberMessage(
        teamBottariId: Long,
        memberId: Long,
    ): Result<Unit> = safeApiCall { teamBottariService.sendRemindByMemberMessage(teamBottariId, memberId) }

    override suspend fun joinTeamBottari(request: TeamBottariJoinRequest): Result<Unit> =
        safeApiCall { teamBottariService.joinTeamBottari(request) }

    override suspend fun fetchTeamBottariMembers(teamBottariId: Long): Result<List<TeamMemberNameFetchResponse>> =
        safeApiCall { teamBottariService.fetchTeamBottariMembers(teamBottariId) }

    override suspend fun fetchTeamAssignedItems(teamBottariId: Long): Result<List<AssignedItemsFetchResponse>> =
        safeApiCall { teamBottariService.fetchTeamAssignedItems(teamBottariId) }

    override suspend fun fetchTeamSharedItems(teamBottariId: Long): Result<List<SharedItemsFetchResponse>> =
        safeApiCall { teamBottariService.fetchTeamSharedItems(teamBottariId) }

    override suspend fun fetchTeamPersonalItems(teamBottariId: Long): Result<List<PersonalItemsFetchResponse>> =
        safeApiCall { teamBottariService.fetchTeamPersonalItems(teamBottariId) }

    override suspend fun saveTeamBottariAssignedItem(
        teamBottariId: Long,
        assignedItemId: Long,
        request: AssignedItemsUpdateRequest,
    ): Result<Unit> =
        safeApiCall {
            teamBottariService.saveTeamAssignedItem(teamBottariId, assignedItemId, request)
        }

    override suspend fun exitTeamBottari(teamBottariId: Long): Result<Unit> =
        safeApiCall { teamBottariService.exitTeamBottari(teamBottariId) }

    companion object {
        private const val HEADER_TEAM_BOTTARI_ID_PREFIX = "/team-bottaries/"
    }
}
