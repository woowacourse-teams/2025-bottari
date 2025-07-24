package com.bottari.data.service

import com.bottari.data.model.template.FetchBottariTemplateResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface BottariTemplateService {
    @GET("/templates")
    suspend fun fetchBottariTemplates(
        @Query("query") searchWord: String?,
    ): Response<List<FetchBottariTemplateResponse>>
}
