package com.bottari.core.network.service

import com.bottari.core.network.dto.team.bottari.TeamBottariCreateRequest
import com.bottari.core.network.dto.team.bottari.TeamBottariDetailFetchResponse
import com.bottari.core.network.dto.team.bottari.TeamBottariFetchResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface TeamBottariService {
    @POST("/team-bottaries")
    suspend fun createTeamBottari(
        @Body request: TeamBottariCreateRequest,
    ): Response<Unit>

    @GET("/team-bottaries")
    suspend fun fetchTeamBottaries(): Response<List<TeamBottariFetchResponse>>

    @GET("/team-bottaries/{teamBottariId}")
    suspend fun fetchTeamBottariDetail(
        @Path("teamBottariId") teamBottariId: Long,
    ): Response<TeamBottariDetailFetchResponse>

    @DELETE("/team-bottaries/{id}")
    suspend fun exitTeamBottari(
        @Path("id") teamBottariId: Long,
    ): Response<Unit>
}
