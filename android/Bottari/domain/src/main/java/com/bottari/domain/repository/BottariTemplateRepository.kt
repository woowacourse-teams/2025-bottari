package com.bottari.domain.repository

import com.bottari.domain.model.bottari.template.BottariTemplate
import com.bottari.domain.model.exception.BottariResult

interface BottariTemplateRepository {
    suspend fun fetchBottariTemplates(searchWord: String?): BottariResult<List<BottariTemplate>>

    suspend fun createBottariTemplate(
        title: String,
        items: List<String>,
    ): BottariResult<Long>

    suspend fun fetchBottariTemplate(bottariId: Long): BottariResult<BottariTemplate>

    suspend fun takeBottariTemplate(bottariId: Long): BottariResult<Long>

    suspend fun fetchMyBottariTemplates(): BottariResult<List<BottariTemplate>>

    suspend fun deleteMyBottariTemplate(bottariTemplateId: Long): BottariResult<Unit>
}
