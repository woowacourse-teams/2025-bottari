package com.bottari.data.source.remote

import com.bottari.data.model.remote.report.TemplateReportRequest

interface ReportRemoteDataSource {
    suspend fun reportTemplate(
        bottariTemplateId: Long,
        request: TemplateReportRequest,
    ): Result<Unit>
}
