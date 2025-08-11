package com.bottari.domain.repository

interface ReportRepository {
    suspend fun reportTemplate(
        bottariTemplateId: Long,
        reason: String,
    ): Result<Unit>
}
