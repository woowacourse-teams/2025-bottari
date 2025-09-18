package com.bottari.data.repository

import com.bottari.data.model.remote.bottari.template.BottariTemplateCreateRequest
import com.bottari.data.source.remote.BottariTemplateRemoteDataSource
import com.bottari.domain.model.bottari.template.BottariTemplate
import com.bottari.domain.model.exception.BottariResult
import com.bottari.domain.model.exception.mapCatching
import com.bottari.domain.repository.BottariTemplateRepository

class BottariTemplateRepositoryImpl(
    private val bottariTemplateRemoteDataSource: BottariTemplateRemoteDataSource,
) : BottariTemplateRepository {
    override suspend fun fetchBottariTemplates(searchWord: String?): BottariResult<List<BottariTemplate>> =
        bottariTemplateRemoteDataSource
            .fetchBottariTemplates(searchWord)
            .mapCatching { response -> response.map { it.toDomain() } }

    override suspend fun createBottariTemplate(
        title: String,
        items: List<String>,
    ): BottariResult<Long> =
        bottariTemplateRemoteDataSource
            .createBottariTemplate(
                BottariTemplateCreateRequest(
                    items,
                    title,
                ),
            )

    override suspend fun fetchBottariTemplate(bottariId: Long): BottariResult<BottariTemplate> =
        bottariTemplateRemoteDataSource
            .fetchBottariTemplateDetail(bottariId)
            .mapCatching { it.toDomain() }

    override suspend fun takeBottariTemplate(bottariId: Long): BottariResult<Long> =
        bottariTemplateRemoteDataSource.takeBottariTemplate(bottariId)

    override suspend fun fetchMyBottariTemplates(): BottariResult<List<BottariTemplate>> =
        bottariTemplateRemoteDataSource
            .fetchMyBottariTemplates()
            .mapCatching { response -> response.map { it.toDomain() } }

    override suspend fun deleteMyBottariTemplate(bottariTemplateId: Long): BottariResult<Unit> =
        bottariTemplateRemoteDataSource.deleteMyBottariTemplate(bottariTemplateId)
}
