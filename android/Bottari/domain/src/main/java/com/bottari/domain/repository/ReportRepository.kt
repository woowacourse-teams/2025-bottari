package com.bottari.domain.repository

interface ReportRepository {
    suspend fun reportTemplate(
        ssaid: String,
        bottariTemplateId: Long,
        reason: String,
    ): Result<Unit>
}
