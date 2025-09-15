package com.bottari.data.service

import com.bottari.data.model.bottari.BottariCreateRequest
import com.bottari.data.model.bottari.BottariFetchResponse
import com.bottari.data.model.bottari.BottariTitleUpdateRequest
import com.bottari.data.model.bottari.BottariesFetchResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path

interface BottariService {
    @GET("/bottaries")
    suspend fun fetchBottaries(): Response<List<BottariesFetchResponse>>

    @GET("/bottaries/{id}")
    suspend fun fetchBottari(
        @Path("id") id: Long,
    ): Response<BottariFetchResponse>

    @POST("/bottaries")
    suspend fun createBottari(
        @Body request: BottariCreateRequest,
    ): Response<Unit>

    @DELETE("/bottaries/{id}")
    suspend fun deleteBottari(
        @Path("id") id: Long,
    ): Response<Unit>

    @PATCH("/bottaries/{id}")
    suspend fun saveBottariTitle(
        @Path("id") id: Long,
        @Body request: BottariTitleUpdateRequest,
    ): Response<Unit>
}
