package com.bottari.data.service

import com.bottari.data.model.template.FetchBottariTemplateResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface BottariTemplateService {
    @GET("/templates")
    suspend fun fetchBottariTemplates(
        @Query("query") searchWord: String?,
    ): Response<List<FetchBottariTemplateResponse>>

    @GET("/templates/{bottariId}")
    suspend fun fetchBottariTemplateDetail(
        @Path("bottariId") bottariId: Long,
    ): Response<FetchBottariTemplateResponse>

    @POST("/templates/{bottariId}/create-bottari")
    suspend fun takeBottariTemplate(
        @Header("ssaid") ssaid: String,
        @Path("bottariId") bottariId: Long,
    ): Response<Unit>
}
