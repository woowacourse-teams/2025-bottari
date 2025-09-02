package com.bottari.data.service

import com.bottari.data.model.bottari.BottariRequest
import com.bottari.data.model.bottari.BottariResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path

interface BottariService {
    @GET("/bottaries")
    suspend fun fetchBottaries(): Response<List<BottariResponse.FetchBottariesResponse>>

    @GET("/bottaries/{id}")
    suspend fun findBottari(
        @Path("id") id: Long,
    ): Response<BottariResponse.FetchBottariResponse>

    @POST("/bottaries")
    suspend fun createBottari(
        @Body request: BottariRequest.CreateBottariRequest,
    ): Response<Unit>

    @DELETE("/bottaries/{id}")
    suspend fun deleteBottari(
        @Path("id") id: Long,
    ): Response<Unit>

    @PATCH("/bottaries/{id}")
    suspend fun saveBottariTitle(
        @Path("id") id: Long,
        @Body request: BottariRequest.UpdateBottariTitleRequest,
    ): Response<Unit>
}
