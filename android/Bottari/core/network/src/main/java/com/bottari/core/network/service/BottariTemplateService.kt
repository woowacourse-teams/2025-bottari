package com.bottari.core.network.service

import com.bottari.core.network.dto.bottari.template.BottariTemplateCreateRequest
import com.bottari.core.network.dto.bottari.template.BottariTemplateCursorFetchResponse
import com.bottari.core.network.dto.bottari.template.BottariTemplateFetchResponse
import com.bottari.core.network.dto.common.PageableResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.QueryMap

interface BottariTemplateService {
    @GET("/templates/title")
    suspend fun searchTemplatesByTitle(
        @QueryMap params: Map<String, String>,
    ): Response<PageableResponse<BottariTemplateCursorFetchResponse>>

    @GET("/templates/hashtag")
    suspend fun searchTemplatesByHashtag(
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
