package com.bottari.data.source.remote

import com.bottari.data.common.extension.extractIdFromHeader
import com.bottari.data.model.common.ErrorResponse
import com.bottari.data.model.team.CreateTeamBottariRequest
import com.bottari.data.service.TeamBottariService

interface TeamBottariRemoteDataSource {
    suspend fun createBottari(request: CreateTeamBottariRequest): Result<Long?>
}
