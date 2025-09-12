package com.bottari.data.source.remote

import com.bottari.data.common.util.safeApiCall
import com.bottari.data.model.report.TemplateReportRequest
import com.bottari.data.service.ReportService

class ReportRemoteDataSourceImpl(
    private val reportService: ReportService,
) : ReportRemoteDataSource {
    override suspend fun reportTemplate(
        bottariTemplateId: Long,
        request: TemplateReportRequest,
    ): Result<Unit> = safeApiCall { reportService.reportTemplate(bottariTemplateId, request) }
}
