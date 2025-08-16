package com.bottari.data.source.remote

import com.bottari.data.common.extension.extractIdFromHeader
import com.bottari.data.common.util.safeApiCall
import com.bottari.data.model.common.ErrorResponse
import com.bottari.data.model.team.CreateTeamBottariRequest
import com.bottari.data.model.team.FetchTeamBottariChecklistResponse
import com.bottari.data.model.team.FetchTeamBottariDetailResponse
import com.bottari.data.model.team.FetchTeamBottariResponse
import com.bottari.data.model.team.FetchTeamBottariStatusResponse
import com.bottari.data.model.team.FetchTeamMemberStatusResponse
import com.bottari.data.model.team.FetchTeamMembersResponse
import com.bottari.data.model.team.ItemTypeRequest
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

    override suspend fun remindTeamBottariItem(
        id: Long,
        type: ItemTypeRequest,
    ): Result<Unit> =
        safeApiCall {
            teamBottariService.remindTeamBottariItem(id, type)
        }

    override suspend fun fetchTeamMembers(id: Long): Result<FetchTeamMembersResponse> =
        safeApiCall { teamBottariService.fetchTeamMembers(id) }

    override suspend fun fetchTeamMembersStatus(id: Long): Result<List<FetchTeamMemberStatusResponse>> =
        safeApiCall { teamBottariService.fetchTeamMembersStatus(id) }

    override suspend fun sendRemindByMemberMessage(
        teamBottariId: Long,
        memberId: Long,
    ): Result<Unit> = safeApiCall { teamBottariService.sendRemindByMemberMessage(teamBottariId, memberId) }

    companion object {
        private const val HEADER_TEAM_BOTTARI_ID_PREFIX = "/team-bottaries/"
    }
}
