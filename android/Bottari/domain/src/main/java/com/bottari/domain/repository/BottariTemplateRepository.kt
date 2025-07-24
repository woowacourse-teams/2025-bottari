package com.bottari.domain.repository

import com.bottari.domain.model.template.BottariTemplate

interface BottariTemplateRepository {
    suspend fun fetchBottariTemplates(searchWord: String?): Result<List<BottariTemplate>>

    suspend fun fetchBottariTemplate(bottariId: Long): Result<BottariTemplate>

    suspend fun takeBottariTemplate(
        ssaid: String,
        bottariId: Long,
    ): Result<Long>
}
