package com.bottari.data.source.remote

import com.bottari.data.model.team.CreateTeamBottariRequest
import com.bottari.data.model.team.FetchTeamBottariResponse

interface TeamBottariRemoteDataSource {
    suspend fun createBottari(request: CreateTeamBottariRequest): Result<Long?>

    suspend fun fetchTeamBottaries(): Result<List<FetchTeamBottariResponse>>
}
