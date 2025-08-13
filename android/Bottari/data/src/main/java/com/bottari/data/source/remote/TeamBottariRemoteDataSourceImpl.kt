package com.bottari.data.source.remote

import com.bottari.data.common.extension.extractIdFromHeader
import com.bottari.data.common.util.safeApiCall
import com.bottari.data.model.common.ErrorResponse
import com.bottari.data.model.team.CreateTeamBottariRequest
import com.bottari.data.model.team.FetchTeamBottariChecklistResponse
import com.bottari.data.model.team.FetchTeamBottariResponse
import com.bottari.data.model.team.ItemTypeRequest
import com.bottari.data.model.team.TeamMembersResponse
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
            val response = teamBottariService.fetchTeamBottari(teamBottariId)
            if (response.isSuccessful) {
                return Result.success(response.body()!!)
            }

            val errorResponse = ErrorResponse.parseErrorResponse(response.errorBody())
            return Result.failure(Exception(errorResponse?.title))
        }

    override suspend fun uncheckBottariItem(
        bottariItemId: Long,
        request: ItemTypeRequest,
    ): Result<Unit> =
        safeApiCall {
            val response = teamBottariService.uncheckTeamBottariItem(bottariItemId, request)
            if (response.isSuccessful) {
                return Result.success(Unit)
            }
            val errorResponse = ErrorResponse.parseErrorResponse(response.errorBody())
            return Result.failure(Exception(errorResponse?.title))
        }

    override suspend fun checkBottariItem(
        bottariItemId: Long,
        request: ItemTypeRequest,
    ): Result<Unit> =
        safeApiCall {
            val response = teamBottariService.checkTeamBottariItem(bottariItemId, request)
            if (response.isSuccessful) {
                return Result.success(Unit)
            }
            val errorResponse = ErrorResponse.parseErrorResponse(response.errorBody())
            return Result.failure(Exception(errorResponse?.title))
        }

    override suspend fun fetchTeamBottaries(): Result<List<FetchTeamBottariResponse>> =
        safeApiCall {
            teamBottariService.fetchTeamBottaries()
        }

    override suspend fun fetchTeamMembers(id: Long): Result<TeamMembersResponse> = safeApiCall { teamBottariService.fetchTeamMembers(id) }

    companion object {
        private const val HEADER_TEAM_BOTTARI_ID_PREFIX = "/team-bottaries/"
    }
}
