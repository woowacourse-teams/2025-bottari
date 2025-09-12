package com.bottari.data.source.remote

import com.bottari.data.model.report.TemplateReportRequest

interface ReportRemoteDataSource {
    suspend fun reportTemplate(
        bottariTemplateId: Long,
        request: TemplateReportRequest,
    ): Result<Unit>
}
