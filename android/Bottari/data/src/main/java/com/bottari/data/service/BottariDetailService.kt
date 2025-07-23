package com.bottari.data.service

import com.bottari.data.model.bottari.BottariResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

interface BottariDetailService {
    @GET("/bottaries/{id}")
    suspend fun findBottari(
        @Path("id") id: Long,
        @Header("ssaid") ssaid: String,
    ): Response<BottariResponse>
}
