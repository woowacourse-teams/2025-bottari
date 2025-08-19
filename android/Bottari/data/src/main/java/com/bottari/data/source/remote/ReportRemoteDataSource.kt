package com.bottari.data.source.remote

import com.bottari.data.model.report.ReportTemplateRequest

interface ReportRemoteDataSource {
    suspend fun reportTemplate(
        bottariTemplateId: Long,
        request: ReportTemplateRequest,
    ): Result<Unit>
}
