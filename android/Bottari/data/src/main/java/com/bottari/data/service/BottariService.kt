package com.bottari.data.service

import com.bottari.data.model.bottari.BottariResponse
import com.bottari.data.model.bottari.FetchBottariesResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

interface BottariService {
    @GET("/bottaries")
    suspend fun fetchBottaries(
        @Header("ssaid") ssaid: String,
    ): Response<List<FetchBottariesResponse>>

    @GET("/bottaries/{id}")
    suspend fun findBottari(
        @Header("ssaid") ssaid: String,
        @Path("id") id: Long,
    ): Response<BottariResponse>
}
