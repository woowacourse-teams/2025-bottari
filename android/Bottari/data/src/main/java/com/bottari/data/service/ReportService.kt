package com.bottari.data.service

import com.bottari.data.model.remote.report.TemplateReportRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Path

interface ReportService {
    @POST("/reports/templates/{bottariTemplateId}")
    suspend fun reportTemplate(
        @Path("bottariTemplateId") bottariTemplateId: Long,
        @Body request: TemplateReportRequest,
    ): Response<Unit>
}
