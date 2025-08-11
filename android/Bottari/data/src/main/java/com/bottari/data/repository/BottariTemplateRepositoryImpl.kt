package com.bottari.data.repository

import com.bottari.data.mapper.BottariTemplateMapper.toDomain
import com.bottari.data.model.template.CreateBottariTemplateRequest
import com.bottari.data.source.remote.BottariTemplateRemoteDataSource
import com.bottari.domain.model.template.BottariTemplate
import com.bottari.domain.repository.BottariTemplateRepository

class BottariTemplateRepositoryImpl(
    private val bottariTemplateRemoteDataSource: BottariTemplateRemoteDataSource,
) : BottariTemplateRepository {
    override suspend fun fetchBottariTemplates(searchWord: String?): Result<List<BottariTemplate>> =
        bottariTemplateRemoteDataSource
            .fetchBottariTemplates(searchWord)
            .mapCatching { response -> response.map { it.toDomain() } }

    override suspend fun createBottariTemplate(
        title: String,
        items: List<String>,
    ): Result<Long?> =
        bottariTemplateRemoteDataSource
            .createBottariTemplate(CreateBottariTemplateRequest(items, title))

    override suspend fun fetchBottariTemplate(bottariId: Long): Result<BottariTemplate> =
        bottariTemplateRemoteDataSource
            .fetchBottariTemplateDetail(bottariId)
            .mapCatching { it.toDomain() }

    override suspend fun takeBottariTemplate(bottariId: Long): Result<Long?> =
        bottariTemplateRemoteDataSource.takeBottariTemplate(bottariId)

    override suspend fun fetchMyBottariTemplates(): Result<List<BottariTemplate>> =
        bottariTemplateRemoteDataSource
            .fetchMyBottariTemplates()
            .mapCatching { response -> response.map { it.toDomain() } }

    override suspend fun deleteMyBottariTemplate(bottariTemplateId: Long): Result<Unit> =
        bottariTemplateRemoteDataSource.deleteMyBottariTemplate(bottariTemplateId)
}
