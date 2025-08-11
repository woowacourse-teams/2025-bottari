package com.bottari.domain.repository

import com.bottari.domain.model.template.BottariTemplate

interface BottariTemplateRepository {
    suspend fun fetchBottariTemplates(searchWord: String?): Result<List<BottariTemplate>>

    suspend fun createBottariTemplate(
        title: String,
        items: List<String>,
    ): Result<Long?>

    suspend fun fetchBottariTemplate(bottariId: Long): Result<BottariTemplate>

    suspend fun takeBottariTemplate(bottariId: Long): Result<Long?>

    suspend fun fetchMyBottariTemplates(): Result<List<BottariTemplate>>

    suspend fun deleteMyBottariTemplate(bottariTemplateId: Long): Result<Unit>
}
