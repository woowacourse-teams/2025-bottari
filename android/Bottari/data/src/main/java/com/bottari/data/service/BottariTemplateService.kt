package com.bottari.data.service

import com.bottari.data.model.template.CreateBottariTemplateRequest
import com.bottari.data.model.template.FetchBottariTemplateResponse
import com.bottari.data.model.template.FetchMyBottariTemplatesResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
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

    @POST("/templates")
    suspend fun createBottariTemplate(
        @Header("ssaid") ssaid: String,
        @Body request: CreateBottariTemplateRequest,
    ): Response<Unit>

    @GET("/templates/{bottariId}")
    suspend fun fetchBottariTemplateDetail(
        @Path("bottariId") bottariId: Long,
    ): Response<FetchBottariTemplateResponse>

    @POST("/templates/{bottariId}/create-bottari")
    suspend fun takeBottariTemplate(
        @Header("ssaid") ssaid: String,
        @Path("bottariId") bottariId: Long,
    ): Response<Unit>

    @GET("/templates/me")
    suspend fun fetchMyBottariTemplates(
        @Header("ssaid") ssaid: String,
    ): Response<List<FetchMyBottariTemplatesResponse>>

    @DELETE("/templates/{id}")
    suspend fun deleteMyBottariTemplate(
        @Header("ssaid") ssaid: String,
        @Path("id") bottariTemplateId: Long,
    ): Response<Unit>
}
