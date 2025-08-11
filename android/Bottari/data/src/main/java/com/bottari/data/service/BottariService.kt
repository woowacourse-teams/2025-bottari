package com.bottari.data.service

import com.bottari.data.model.bottari.BottariResponse
import com.bottari.data.model.bottari.CreateBottariRequest
import com.bottari.data.model.bottari.FetchBottariesResponse
import com.bottari.data.model.bottari.UpdateBottariTitleRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path

interface BottariService {
    @GET("/bottaries")
    suspend fun fetchBottaries(): Response<List<FetchBottariesResponse>>

    @GET("/bottaries/{id}")
    suspend fun findBottari(
        @Path("id") id: Long,
    ): Response<BottariResponse>

    @POST("/bottaries")
    suspend fun createBottari(
        @Body request: CreateBottariRequest,
    ): Response<Unit>

    @DELETE("/bottaries/{id}")
    suspend fun deleteBottari(
        @Path("id") id: Long,
    ): Response<Unit>

    @PATCH("/bottaries/{id}")
    suspend fun saveBottariTitle(
        @Path("id") id: Long,
        @Body request: UpdateBottariTitleRequest,
    ): Response<Unit>
}
