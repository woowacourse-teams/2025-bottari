package com.bottari.data.service.team.bottari

import com.bottari.data.model.remote.team.bottari.TeamBottariCreateRequest
import com.bottari.data.model.remote.team.bottari.TeamBottariDetailFetchResponse
import com.bottari.data.model.remote.team.bottari.TeamBottariFetchResponse
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
