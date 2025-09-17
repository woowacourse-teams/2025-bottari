package com.bottari.data.service

import com.bottari.data.model.remote.bottari.template.BottariTemplateCreateRequest
import com.bottari.data.model.remote.bottari.template.BottariTemplateFetchResponse
import com.bottari.domain.model.exception.BottariResult
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface BottariTemplateService {
    @GET("/templates")
    suspend fun fetchBottariTemplates(
        @Query("query") searchWord: String?,
    ): BottariResult<List<BottariTemplateFetchResponse>>

    @POST("/templates")
    suspend fun createBottariTemplate(
        @Body request: BottariTemplateCreateRequest,
    ): BottariResult<Long>

    @GET("/templates/{bottariId}")
    suspend fun fetchBottariTemplateDetail(
        @Path("bottariId") bottariId: Long,
    ): BottariResult<BottariTemplateFetchResponse>

    @POST("/templates/{bottariId}/create-bottari")
    suspend fun takeBottariTemplate(
        @Path("bottariId") bottariId: Long,
    ): BottariResult<Long>

    @GET("/templates/me")
    suspend fun fetchMyBottariTemplates(): BottariResult<List<BottariTemplateFetchResponse>>

    @DELETE("/templates/{id}")
    suspend fun deleteMyBottariTemplate(
        @Path("id") bottariTemplateId: Long,
    ): BottariResult<Unit>
}
