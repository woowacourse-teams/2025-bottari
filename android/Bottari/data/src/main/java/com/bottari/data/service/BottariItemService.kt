package com.bottari.data.service

import com.bottari.data.model.remote.bottari.item.ItemFetchResponse
import com.bottari.data.model.remote.bottari.item.ItemsSaveRequest
import com.bottari.domain.model.exception.BottariResult
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.Path

interface BottariItemService {
    @GET("/bottaries/{bottariId}/bottari-items")
    suspend fun fetchChecklist(
        @Path("bottariId") bottariId: Long,
    ): BottariResult<List<ItemFetchResponse>>

    @PATCH("/bottari-items/{id}/uncheck")
    suspend fun uncheckBottariItem(
        @Path("id") bottariItemId: Long,
    ): BottariResult<Unit>

    @PATCH("/bottari-items/{id}/check")
    suspend fun checkBottariItem(
        @Path("id") bottariItemId: Long,
    ): BottariResult<Unit>

    @PATCH("/bottaries/{bottariId}/bottari-items")
    suspend fun saveBottariItems(
        @Path("bottariId") bottariId: Long,
        @Body request: ItemsSaveRequest,
    ): BottariResult<Unit>

    @PATCH("/bottaries/{bottariId}/bottari-items/reset")
    suspend fun resetBottariItemCheckState(
        @Path("bottariId") bottariId: Long,
    ): BottariResult<Unit>
}
