package com.bottari.core.network.service

import com.bottari.core.network.dto.team.bottari.FetchTeamBottariStatusResponse
import com.bottari.core.network.dto.team.bottari.item.AssignedItemsCreateRequest
import com.bottari.core.network.dto.team.bottari.item.AssignedItemsFetchResponse
import com.bottari.core.network.dto.team.bottari.item.AssignedItemsUpdateRequest
import com.bottari.core.network.dto.team.bottari.item.PersonalItemsCreateRequest
import com.bottari.core.network.dto.team.bottari.item.PersonalItemsFetchResponse
import com.bottari.core.network.dto.team.bottari.item.SharedItemsCreateRequest
import com.bottari.core.network.dto.team.bottari.item.SharedItemsFetchResponse
import com.bottari.core.network.dto.team.bottari.item.TeamBottariItemCheckUpdateRequest
import com.bottari.core.network.dto.team.bottari.item.TeamBottariItemChecklistFetchResponse
import com.bottari.core.network.dto.team.bottari.item.TeamBottariItemDeleteRequest
import com.bottari.core.network.dto.team.bottari.item.TeamBottariItemRemindRequest
import com.bottari.core.network.dto.team.bottari.item.TeamBottariItemUnCheckUpdateRequest
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
