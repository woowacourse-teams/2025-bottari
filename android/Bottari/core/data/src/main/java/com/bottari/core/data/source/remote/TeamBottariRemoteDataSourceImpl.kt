package com.bottari.core.data.source.remote

import com.bottari.core.data.common.safeApiCall
import com.bottari.core.network.dto.common.ErrorResponse
import com.bottari.core.network.dto.team.bottari.TeamBottariCreateRequest
import com.bottari.core.network.dto.team.bottari.TeamBottariDetailFetchResponse
import com.bottari.core.network.dto.team.bottari.TeamBottariFetchResponse
import com.bottari.core.network.service.TeamBottariService
import com.bottari.data.common.extension.extractIdFromHeader
import javax.inject.Inject

class TeamBottariRemoteDataSourceImpl @Inject constructor(
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

    override suspend fun fetchTeamBottaries(): Result<List<TeamBottariFetchResponse>> =
        safeApiCall {
            teamBottariService.fetchTeamBottaries()
        }

    override suspend fun fetchTeamBottariDetail(teamBottariId: Long): Result<TeamBottariDetailFetchResponse> =
        safeApiCall {
            teamBottariService.fetchTeamBottariDetail(teamBottariId)
        }

    override suspend fun exitTeamBottari(teamBottariId: Long): Result<Unit> =
        safeApiCall { teamBottariService.exitTeamBottari(teamBottariId) }

    companion object {
        private const val HEADER_TEAM_BOTTARI_ID_PREFIX = "/team-bottaries/"
    }
}
