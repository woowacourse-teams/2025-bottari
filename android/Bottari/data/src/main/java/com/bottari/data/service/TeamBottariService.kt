package com.bottari.data.service

import com.bottari.data.model.team.CreateTeamBottariRequest
import com.bottari.data.model.team.FetchTeamBottariResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface TeamBottariService {
    @POST("/team-bottaries")
    suspend fun createTeamBottari(
        @Body request: CreateTeamBottariRequest,
    ): Response<Unit>

    @GET("/team-bottaries")
    suspend fun fetchTeamBottaries(): Response<List<FetchTeamBottariResponse>>
}
