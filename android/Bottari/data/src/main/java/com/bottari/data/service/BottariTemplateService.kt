package com.bottari.data.service

import com.bottari.data.model.remote.bottari.template.BottariTemplateCreateRequest
import com.bottari.data.model.remote.bottari.template.BottariTemplateCursorFetchResponse
import com.bottari.data.model.remote.bottari.template.BottariTemplateFetchResponse
import com.bottari.data.model.remote.common.PageableResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.QueryMap

interface BottariTemplateService {
    @GET("/templates/cursor")
    suspend fun fetchBottariTemplates(
        @QueryMap params: Map<String, String>,
    ): Response<PageableResponse<BottariTemplateCursorFetchResponse>>

    @POST("/templates")
    suspend fun createBottariTemplate(
        @Body request: BottariTemplateCreateRequest,
    ): Response<Unit>

    @GET("/templates/{bottariId}")
    suspend fun fetchBottariTemplateDetail(
        @Path("bottariId") bottariId: Long,
    ): Response<BottariTemplateFetchResponse>

    @POST("/templates/{bottariId}/create-bottari")
    suspend fun takeBottariTemplate(
        @Path("bottariId") bottariId: Long,
    ): Response<Unit>

    @GET("/templates/me")
    suspend fun fetchMyBottariTemplates(): Response<List<BottariTemplateFetchResponse>>

    @DELETE("/templates/{id}")
    suspend fun deleteMyBottariTemplate(
        @Path("id") bottariTemplateId: Long,
    ): Response<Unit>
}
