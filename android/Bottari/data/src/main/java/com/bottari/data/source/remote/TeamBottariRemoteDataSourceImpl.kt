package com.bottari.data.source.remote

import com.bottari.data.common.extension.extractIdFromHeader
import com.bottari.data.model.common.ErrorResponse
import com.bottari.data.model.team.CreateTeamBottariRequest
import com.bottari.data.service.TeamBottariService

class TeamBottariRemoteDataSourceImpl(
    private val teamBottariService: TeamBottariService,
) : TeamBottariRemoteDataSource {
    override suspend fun createBottari(request: CreateTeamBottariRequest): Result<Long?> =
        runCatching {
            val response = teamBottariService.createTeamBottari(request)
            if (!response.isSuccessful) {
                return Result.success(response.extractIdFromHeader(HEADER_TEAM_BOTTARI_ID_PREFIX))
            }

            val errorResponse = ErrorResponse.parseErrorResponse(response.errorBody())
            return Result.failure(Exception(errorResponse?.title))
        }

    companion object {
        private const val HEADER_TEAM_BOTTARI_ID_PREFIX = "/team-bottaries/"
    }
}
