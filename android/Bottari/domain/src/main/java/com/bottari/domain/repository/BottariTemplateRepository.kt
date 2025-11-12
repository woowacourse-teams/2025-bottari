package com.bottari.domain.repository

import com.bottari.domain.model.bottari.template.BottariTemplate
import com.bottari.domain.model.common.Pageable

interface BottariTemplateRepository {
    suspend fun searchTemplatesByTitle(
        title: String,
        pageable: Pageable<BottariTemplate>,
    ): Result<Pageable<BottariTemplate>>

    suspend fun searchTemplatesByHashtag(
        hashtagId: Long,
        pageable: Pageable<BottariTemplate>,
    ): Result<Pageable<BottariTemplate>>

    suspend fun createBottariTemplate(
        title: String,
        description: String,
        items: List<String>,
        hashtag: List<String>,
    ): Result<Long>

    suspend fun fetchBottariTemplate(bottariId: Long): Result<BottariTemplate>

    suspend fun takeBottariTemplate(bottariId: Long): Result<Long?>

    suspend fun fetchMyBottariTemplates(): Result<List<BottariTemplate>>

    suspend fun deleteMyBottariTemplate(bottariTemplateId: Long): Result<Unit>
}
