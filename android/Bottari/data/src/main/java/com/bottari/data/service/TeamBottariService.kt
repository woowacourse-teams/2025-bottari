package com.bottari.data.service

import com.bottari.data.model.team.CreateTeamBottariRequest
import com.bottari.data.model.team.FetchTeamBottariChecklistResponse
import com.bottari.data.model.team.FetchTeamBottariDetailResponse
import com.bottari.data.model.team.FetchTeamBottariResponse
import com.bottari.data.model.team.FetchTeamBottariStatusResponse
import com.bottari.data.model.team.FetchTeamMemberStatusResponse
import com.bottari.data.model.team.FetchTeamMembersResponse
import com.bottari.data.model.team.ItemTypeRequest
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

    @GET("/team-bottaries/{teamBottariId}/checklist")
    suspend fun fetchTeamBottari(
        @Path("teamBottariId") teamBottariId: Long,
    ): Response<FetchTeamBottariChecklistResponse>

    @PATCH("/team-items/{id}/check")
    suspend fun checkTeamBottariItem(
        @Path("id") id: Long,
        @Body request: ItemTypeRequest,
    ): Response<Unit>

    @PATCH("/team-items/{id}/uncheck")
    suspend fun uncheckTeamBottariItem(
        @Path("id") id: Long,
        @Body request: ItemTypeRequest,
    ): Response<Unit>

    @POST("/team-items/{id}/remind")
    suspend fun remind(
        @Path("id") id: Long,
        @Body request: ItemTypeRequest,
    ): Response<Unit>

    @GET("/team-bottaries")
    suspend fun fetchTeamBottaries(): Response<List<FetchTeamBottariResponse>>

    @GET("/team-bottaries/{teamBottariId}/members")
    suspend fun fetchTeamMembers(
        @Path("teamBottariId") id: Long,
    ): Response<FetchTeamMembersResponse>

    @GET("/team-bottaries/{id}")
    suspend fun fetchTeamBottariDetail(
        @Path("id") teamBottariId: Long,
    ): Response<FetchTeamBottariDetailResponse>

    @GET("/team-bottaries/{id}/items/status")
    suspend fun fetchTeamBottariStatus(
        @Path("id") id: Long,
    ): Response<FetchTeamBottariStatusResponse>

    @GET("/team-bottaries/{teamBottariId}/members/status")
    suspend fun fetchTeamMembersStatus(
        @Path("teamBottariId") id: Long,
    ): Response<List<FetchTeamMemberStatusResponse>>

}
