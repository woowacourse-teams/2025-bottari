package com.bottari.data.repository

import com.bottari.data.model.remote.bottari.template.BottariTemplateCreateRequest
import com.bottari.data.model.remote.common.PageableRequest
import com.bottari.data.source.remote.BottariTemplateRemoteDataSource
import com.bottari.domain.model.bottari.template.BottariTemplate
import com.bottari.domain.model.common.Pageable
import com.bottari.domain.repository.BottariTemplateRepository
import javax.inject.Inject

class BottariTemplateRepositoryImpl @Inject constructor(
    private val bottariTemplateRemoteDataSource: BottariTemplateRemoteDataSource,
) : BottariTemplateRepository {
    override suspend fun fetchBottariTemplates(
        query: String?,
        pageable: Pageable<BottariTemplate>,
    ): Result<Pageable<BottariTemplate>> =
        bottariTemplateRemoteDataSource
            .fetchBottariTemplates(PageableRequest.of(query, pageable))
            .mapCatching { response -> response.toDomain { contents -> contents.toDomain() } }

    override suspend fun createBottariTemplate(
        title: String,
        items: List<String>,
    ): Result<Long?> =
        bottariTemplateRemoteDataSource
            .createBottariTemplate(
                BottariTemplateCreateRequest(
                    items,
                    title,
                ),
            )

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
