package com.bottari.data.service

import com.bottari.data.model.remote.team.bottari.FetchTeamBottariStatusResponse
import com.bottari.data.model.remote.team.bottari.item.request.AssignedItemsCreateRequest
import com.bottari.data.model.remote.team.bottari.item.request.AssignedItemsUpdateRequest
import com.bottari.data.model.remote.team.bottari.item.request.PersonalItemsCreateRequest
import com.bottari.data.model.remote.team.bottari.item.request.SharedItemsCreateRequest
import com.bottari.data.model.remote.team.bottari.item.request.TeamBottariItemCheckUpdateRequest
import com.bottari.data.model.remote.team.bottari.item.request.TeamBottariItemDeleteRequest
import com.bottari.data.model.remote.team.bottari.item.request.TeamBottariItemRemindRequest
import com.bottari.data.model.remote.team.bottari.item.request.TeamBottariItemUnCheckUpdateRequest
import com.bottari.data.model.remote.team.bottari.item.response.AssignedItemsFetchResponse
import com.bottari.data.model.remote.team.bottari.item.response.PersonalItemsFetchResponse
import com.bottari.data.model.remote.team.bottari.item.response.SharedItemsFetchResponse
import com.bottari.data.model.remote.team.bottari.item.response.TeamBottariItemChecklistFetchResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.HTTP
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface TeamBottariItemsService {
    @PATCH("/team-items/{itemId}/check")
    suspend fun checkTeamBottariItem(
        @Path("itemId") id: Long,
        @Body request: TeamBottariItemCheckUpdateRequest,
    ): Response<Unit>

    @PATCH("/team-items/{itemId}/uncheck")
    suspend fun uncheckTeamBottariItem(
        @Path("itemId") id: Long,
        @Body request: TeamBottariItemUnCheckUpdateRequest,
    ): Response<Unit>

    @GET("/team-bottaries/{teamBottariId}/checklist")
    suspend fun fetchTeamBottari(
        @Path("teamBottariId") teamBottariId: Long,
    ): Response<TeamBottariItemChecklistFetchResponse>

    @POST("/team-items/{itemId}/remind")
    suspend fun sendRemindByItem(
        @Path("itemId") id: Long,
        @Body request: TeamBottariItemRemindRequest,
    ): Response<Unit>

    @HTTP(method = "DELETE", path = "team-items/{id}", hasBody = true)
    suspend fun deleteTeamBottariItem(
        @Path("id") id: Long,
        @Body request: TeamBottariItemDeleteRequest,
    ): Response<Unit>

    @GET("/team-bottaries/{teamBottariId}/items/status")
    suspend fun fetchTeamBottariStatus(
        @Path("teamBottariId") id: Long,
    ): Response<FetchTeamBottariStatusResponse>

    @POST("/team-bottaries/{teamBottariId}/shared-items")
    suspend fun createTeamBottariSharedItem(
        @Path("teamBottariId") id: Long,
        @Body request: SharedItemsCreateRequest,
    ): Response<Unit>

    @POST("/team-bottaries/{teamBottariId}/personal-items")
    suspend fun createTeamBottariPersonalItem(
        @Path("teamBottariId") id: Long,
        @Body request: PersonalItemsCreateRequest,
    ): Response<Unit>

    @POST("/team-bottaries/{teamBottariId}/assigned-items")
    suspend fun createTeamBottariAssignedItem(
        @Path("teamBottariId") id: Long,
        @Body request: AssignedItemsCreateRequest,
    ): Response<Unit>

    @GET("/team-bottaries/{teamBottariId}/assigned-items")
    suspend fun fetchTeamAssignedItems(
        @Path("teamBottariId") teamBottariId: Long,
    ): Response<List<AssignedItemsFetchResponse>>

    @GET("/team-bottaries/{teamBottariId}/shared-items")
    suspend fun fetchTeamSharedItems(
        @Path("teamBottariId") teamBottariId: Long,
    ): Response<List<SharedItemsFetchResponse>>

    @GET("/team-bottaries/{teamBottariId}/personal-items")
    suspend fun fetchTeamPersonalItems(
        @Path("teamBottariId") teamBottariId: Long,
    ): Response<List<PersonalItemsFetchResponse>>

    @PUT("/team-bottaries/{teamBottariId}/assigned-items/{assignedItemId}")
    suspend fun saveTeamAssignedItem(
        @Path("teamBottariId") teamBottariId: Long,
        @Path("assignedItemId") assignedItemId: Long,
        @Body request: AssignedItemsUpdateRequest,
    ): Response<Unit>
}
