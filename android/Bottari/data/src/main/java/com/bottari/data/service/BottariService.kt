package com.bottari.data.service

import com.bottari.data.model.bottari.CreateBottariRequest
import com.bottari.data.model.bottari.FetchBottariesResponse
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface BottariService {
    @GET("/bottaries")
    suspend fun fetchBottaries(
        @Header("ssaid") ssaid: String,
    ): Response<List<FetchBottariesResponse>>

    @POST("/bottaries")
    suspend fun createBottari(
        @Header("ssaid") ssaid: String,
        @Body body: CreateBottariRequest,
    ): Response<Unit>
}
