package com.bottari.data.service

import retrofit2.Response
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ReportService {
    @POST("/reports/templates/{bottariTemplateId}")
    suspend fun reportTemplate(
        @Path("bottariTemplateId") bottariTemplateId: Long,
        @Query("reason") reason: String,
    ): Response<Unit>
}
