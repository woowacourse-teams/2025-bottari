package com.bottari.data.service

import com.bottari.data.model.team.CreateTeamBottariAssignedItemRequest
import com.bottari.data.model.team.CreateTeamBottariPersonalItemRequest
import com.bottari.data.model.team.CreateTeamBottariRequest
import com.bottari.data.model.team.CreateTeamBottariSharedItemRequest
import com.bottari.data.model.team.FetchTeamBottariChecklistResponse
import com.bottari.data.model.team.FetchTeamBottariDetailResponse
import com.bottari.data.model.team.FetchTeamBottariResponse
import com.bottari.data.model.team.FetchTeamBottariStatusResponse
import com.bottari.data.model.team.FetchTeamMemberStatusResponse
import com.bottari.data.model.team.FetchTeamMembersResponse
import com.bottari.data.model.team.ItemTypeRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
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

    @PATCH("/team-items/{itemId}/check")
    suspend fun checkTeamBottariItem(
        @Path("itemId") id: Long,
        @Body request: ItemTypeRequest,
    ): Response<Unit>

    @PATCH("/team-items/{itemId}/uncheck")
    suspend fun uncheckTeamBottariItem(
        @Path("itemId") id: Long,
        @Body request: ItemTypeRequest,
    ): Response<Unit>

    @POST("/team-items/{itemId}/remind")
    suspend fun sendRemindByItem(
        @Path("itemId") id: Long,
        @Body request: ItemTypeRequest,
    ): Response<Unit>

    @GET("/team-bottaries")
    suspend fun fetchTeamBottaries(): Response<List<FetchTeamBottariResponse>>

    @GET("/team-bottaries/{teamBottariId}/members")
    suspend fun fetchTeamMembers(
        @Path("teamBottariId") id: Long,
    ): Response<FetchTeamMembersResponse>

    @GET("/team-bottaries/{teamBottariId}")
    suspend fun fetchTeamBottariDetail(
        @Path("teamBottariId") teamBottariId: Long,
    ): Response<FetchTeamBottariDetailResponse>

    @GET("/team-bottaries/{teamBottariId}/items/status")
    suspend fun fetchTeamBottariStatus(
        @Path("teamBottariId") id: Long,
    ): Response<FetchTeamBottariStatusResponse>

    @GET("/team-bottaries/{teamBottariId}/members/status")
    suspend fun fetchTeamMembersStatus(
        @Path("teamBottariId") id: Long,
    ): Response<List<FetchTeamMemberStatusResponse>>

    @POST("/team-bottaries/{teamBottariId}/shared-items")
    suspend fun createTeamBottariSharedItem(
        @Path("teamBottariId") id: Long,
        @Body request: CreateTeamBottariSharedItemRequest,
    ): Response<Unit>

    @POST("/team-bottaries/{teamBottariId}/personal-items")
    suspend fun createTeamBottariPersonalItem(
        @Path("teamBottariId") id: Long,
        @Body request: CreateTeamBottariPersonalItemRequest,
    ): Response<Unit>

    @POST("/team-bottaries/{teamBottariId}/assigned-items")
    suspend fun createTeamBottariAssignedItem(
        @Path("teamBottariId") id: Long,
        @Body request: CreateTeamBottariAssignedItemRequest,
    ): Response<Unit>

    @DELETE("/team-items/{id}")
    suspend fun deleteTeamBottariItem(
        @Path("id") id: Long,
    ): Response<Unit>

    @POST("/team-bottaries/{teamBottariId}/members/{memberId}/remind")
    suspend fun sendRemindByMemberMessage(
        @Path("teamBottariId") teamBottariId: Long,
        @Path("memberId") memberId: Long,
    ): Response<Unit>
}
