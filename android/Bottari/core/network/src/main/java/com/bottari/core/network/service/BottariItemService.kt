package com.bottari.core.network.service

import com.bottari.core.network.dto.bottari.item.ItemFetchResponse
import com.bottari.core.network.dto.bottari.item.ItemsSaveRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.Path

interface BottariItemService {
    @GET("/bottaries/{bottariId}/bottari-items")
    suspend fun fetchChecklist(
        @Path("bottariId") bottariId: Long,
    ): Response<List<ItemFetchResponse>>

    @PATCH("/bottari-items/{id}/uncheck")
    suspend fun uncheckBottariItem(
        @Path("id") bottariItemId: Long,
    ): Response<Unit>

    @PATCH("/bottari-items/{id}/check")
    suspend fun checkBottariItem(
        @Path("id") bottariItemId: Long,
    ): Response<Unit>

    @PATCH("/bottaries/{bottariId}/bottari-items")
    suspend fun saveBottariItems(
        @Path("bottariId") bottariId: Long,
        @Body request: ItemsSaveRequest,
    ): Response<Unit>

    @PATCH("/bottaries/{bottariId}/bottari-items/reset")
    suspend fun resetBottariItemCheckState(
        @Path("bottariId") bottariId: Long,
    ): Response<Unit>
}
