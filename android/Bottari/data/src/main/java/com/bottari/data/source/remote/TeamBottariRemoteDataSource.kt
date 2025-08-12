package com.bottari.data.source.remote

import com.bottari.data.model.team.CreateTeamBottariRequest

interface TeamBottariRemoteDataSource {
    suspend fun createBottari(request: CreateTeamBottariRequest): Result<Long?>
}
