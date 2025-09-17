package com.bottari.data.source.remote

import com.bottari.data.common.extension.extractIdFromHeader
import com.bottari.data.common.util.safeApiCall
import com.bottari.data.model.remote.common.ErrorResponse
import com.bottari.data.model.remote.team.bottari.FetchTeamBottariStatusResponse
import com.bottari.data.model.remote.team.bottari.TeamBottariDetailFetchResponse
import com.bottari.data.model.remote.team.bottari.TeamBottariFetchResponse
import com.bottari.data.model.remote.team.bottari.TeamBottariJoinRequest
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
import com.bottari.data.model.remote.team.member.TeamMemberFetchResponse
import com.bottari.data.service.team.bottari.TeamBottariService
import com.bottari.data.service.team.bottari.item.TeamBottariItemsService
import com.bottari.data.service.team.member.TeamMemberService

class TeamBottariRemoteDataSourceImpl(
    private val teamBottariService: TeamBottariService,
    private val teamMemberService: TeamMemberService,
    private val teamBottariItemsService: TeamBottariItemsService,
) : TeamBottariRemoteDataSource {
    override suspend fun createBottari(request: com.bottari.data.model.remote.team.bottari.TeamBottariCreateRequest): Result<Long?> =
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

    override suspend fun fetchTeamBottaries(): Result<List<TeamBottariFetchResponse>> =
        safeApiCall {
            teamBottariService.fetchTeamBottaries()
        }

    override suspend fun fetchTeamBottariDetail(teamBottariId: Long): Result<TeamBottariDetailFetchResponse> =
        safeApiCall {
            teamBottariService.fetchTeamBottariDetail(teamBottariId)
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

    override suspend fun fetchTeamMembers(id: Long): Result<TeamMemberFetchResponse> =
        safeApiCall { teamMemberService.fetchTeamMembers(id) }

    override suspend fun fetchTeamMembersStatus(
        id: Long,
    ): Result<List<com.bottari.data.model.remote.team.member.TeamMemberStatusFetchResponse>> =
        safeApiCall { teamMemberService.fetchTeamMembersStatus(id) }

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

    override suspend fun sendRemindByMemberMessage(
        teamBottariId: Long,
        memberId: Long,
    ): Result<Unit> = safeApiCall { teamMemberService.sendRemindByMemberMessage(teamBottariId, memberId) }

    override suspend fun joinTeamBottari(request: TeamBottariJoinRequest): Result<Unit> =
        safeApiCall { teamMemberService.joinTeamBottari(request) }

    override suspend fun fetchTeamBottariMembers(
        teamBottariId: Long,
    ): Result<List<com.bottari.data.model.remote.team.member.TeamMemberNameFetchResponse>> =
        safeApiCall { teamMemberService.fetchTeamBottariMembers(teamBottariId) }

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

    override suspend fun exitTeamBottari(teamBottariId: Long): Result<Unit> =
        safeApiCall { teamBottariService.exitTeamBottari(teamBottariId) }

    companion object {
        private const val HEADER_TEAM_BOTTARI_ID_PREFIX = "/team-bottaries/"
    }
}
