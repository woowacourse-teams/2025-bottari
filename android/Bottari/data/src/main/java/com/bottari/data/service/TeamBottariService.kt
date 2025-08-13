package com.bottari.data.service

import com.bottari.data.model.team.CreateTeamBottariRequest
import com.bottari.data.model.team.FetchTeamBottariChecklistResponse
import com.bottari.data.model.team.FetchTeamBottariResponse
import com.bottari.data.model.team.ItemTypeRequest
import com.bottari.data.model.team.TeamMembersResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path

interface TeamBottariService {
    @POST("/team-bottaries")
    suspend fun createTeamBottari(
        @Body request: CreateTeamBottariRequest,
    ): Response<Unit>

    @GET("/teams/{teamBottariId}/checklist")
    suspend fun fetchTeamBottari(
        @Path("teamBottariId") teamBottariId: Long,
    ): Response<FetchTeamBottariChecklistResponse>

    @PATCH("/team-items/{id}/check")
    suspend fun checkTeamBottariItem(
        @Path("id") id: Long,
        @Body body: ItemTypeRequest,
    ): Response<Unit>

    @PATCH("/team-items/{id}/uncheck")
    suspend fun uncheckTeamBottariItem(
        @Path("id") id: Long,
        @Body body: ItemTypeRequest,
    ): Response<Unit>

    @GET("/team-bottaries")
    suspend fun fetchTeamBottaries(): Response<List<FetchTeamBottariResponse>>

    @GET("/team-bottaries/{teamBottariId}/members")
    suspend fun fetchTeamMembers(
        @Path("teamBottariId") id: Long,
    ): Response<TeamMembersResponse>
}
