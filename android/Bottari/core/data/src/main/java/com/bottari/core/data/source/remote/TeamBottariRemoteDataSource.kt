package com.bottari.core.data.source.remote

import com.bottari.core.network.dto.team.bottari.TeamBottariCreateRequest
import com.bottari.core.network.dto.team.bottari.TeamBottariDetailFetchResponse
import com.bottari.core.network.dto.team.bottari.TeamBottariFetchResponse

interface TeamBottariRemoteDataSource {
    suspend fun createBottari(request: TeamBottariCreateRequest): Result<Long?>

    suspend fun fetchTeamBottaries(): Result<List<TeamBottariFetchResponse>>

    suspend fun fetchTeamBottariDetail(teamBottariId: Long): Result<TeamBottariDetailFetchResponse>

    suspend fun exitTeamBottari(teamBottariId: Long): Result<Unit>
}
