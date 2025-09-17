package com.bottari.data.source.remote

import com.bottari.data.model.remote.report.TemplateReportRequest
import com.bottari.data.service.ReportService
import com.bottari.domain.model.exception.BottariResult

class ReportRemoteDataSourceImpl(
    private val reportService: ReportService,
) : ReportRemoteDataSource {
    override suspend fun reportTemplate(
        bottariTemplateId: Long,
        request: TemplateReportRequest,
    ): BottariResult<Unit> = reportService.reportTemplate(bottariTemplateId, request)
}
