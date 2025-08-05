package com.bottari.domain.repository

import com.bottari.domain.model.template.BottariTemplate

interface BottariTemplateRepository {
    suspend fun fetchBottariTemplates(searchWord: String?): Result<List<BottariTemplate>>

    suspend fun createBottariTemplate(
        ssaid: String,
        title: String,
        items: List<String>,
    ): Result<Long?>

    suspend fun fetchBottariTemplate(bottariId: Long): Result<BottariTemplate>

    suspend fun takeBottariTemplate(
        ssaid: String,
        bottariId: Long,
    ): Result<Long?>

    suspend fun fetchMyBottariTemplates(ssaid: String): Result<List<BottariTemplate>>

    suspend fun deleteMyBottariTemplate(
        ssaid: String,
        bottariTemplateId: Long,
    ): Result<Unit>
}
