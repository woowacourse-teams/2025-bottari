package com.bottari.data.source.remote

import com.bottari.data.common.extension.extractIdFromHeader
import com.bottari.data.common.util.safeApiCall
import com.bottari.data.model.remote.common.ErrorResponse
import com.bottari.data.model.remote.team.bottari.TeamBottariDetailFetchResponse
import com.bottari.data.model.remote.team.bottari.TeamBottariFetchResponse
import com.bottari.data.service.TeamBottariService
import javax.inject.Inject

class TeamBottariRemoteDataSourceImpl @Inject constructor(
    private val teamBottariService: TeamBottariService,
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
