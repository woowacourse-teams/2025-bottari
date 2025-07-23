package com.bottari.data.service

import com.bottari.data.model.bottari.FetchBottariesResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header

interface BottariService {
    @GET("/bottaries")
    suspend fun fetchBottaries(
        @Header("ssaid") ssaid: String,
    ): Response<List<FetchBottariesResponse>>
}
