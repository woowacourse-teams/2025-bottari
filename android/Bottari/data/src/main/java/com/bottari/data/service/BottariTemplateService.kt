package com.bottari.data.service

import com.bottari.data.model.template.FetchBottariTemplateResponse
import retrofit2.Response
import retrofit2.http.GET

interface BottariTemplateService {
    @GET("/templates")
    suspend fun fetchBottariTemplates(): Response<List<FetchBottariTemplateResponse>>
}
