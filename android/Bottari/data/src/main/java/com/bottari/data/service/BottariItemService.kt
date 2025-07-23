package com.bottari.data.service

import com.bottari.data.model.item.FetchChecklistResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PATCH
import retrofit2.http.Path

interface BottariItemService {
    @GET("/bottaries/{bottariId}/bottari-items")
    suspend fun fetchChecklist(
        @Header("ssaid") ssaid: String,
        @Path("bottariId") bottariId: Long,
    ): Response<List<FetchChecklistResponse>>

    @PATCH("/bottari-items/{id}/uncheck")
    suspend fun uncheckBottariItem(
        @Header("ssaid") ssaid: String,
        @Path("id") bottariItemId: Long,
    ): Response<Unit>

    @PATCH("/bottari-items/{id}/check")
    suspend fun checkBottariItem(
        @Header("ssaid") ssaid: String,
        @Path("id") bottariItemId: Long,
    ): Response<Unit>
}
