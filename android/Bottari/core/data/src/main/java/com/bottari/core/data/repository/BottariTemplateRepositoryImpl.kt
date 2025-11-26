package com.bottari.core.data.repository

import com.bottari.core.data.source.remote.BottariTemplateRemoteDataSource
import com.bottari.core.domain.model.bottari.template.BottariTemplate
import com.bottari.core.domain.model.common.Pageable
import com.bottari.core.domain.repository.BottariTemplateRepository
import com.bottari.core.network.dto.bottari.template.BottariTemplateCreateRequest
import com.bottari.core.network.dto.common.PageableRequest
import javax.inject.Inject

class BottariTemplateRepositoryImpl @Inject constructor(
    private val bottariTemplateRemoteDataSource: BottariTemplateRemoteDataSource,
) : BottariTemplateRepository {
    override suspend fun searchTemplatesByTitle(
        title: String,
        pageable: Pageable<BottariTemplate>,
    ): Result<Pageable<BottariTemplate>> =
        bottariTemplateRemoteDataSource
            .searchTemplatesByTitle(title, PageableRequest.of(pageable))
            .mapCatching { response -> response.toDomain { contents -> contents.toDomain() } }

    override suspend fun searchTemplatesByHashtag(
        hashtagId: Long,
        pageable: Pageable<BottariTemplate>,
    ): Result<Pageable<BottariTemplate>> =
        bottariTemplateRemoteDataSource
            .searchTemplatesByHashtag(hashtagId, PageableRequest.of(pageable))
            .mapCatching { response -> response.toDomain { contents -> contents.toDomain() } }

    override suspend fun createBottariTemplate(
        title: String,
        description: String,
        items: List<String>,
        hashtag: List<String>,
    ): Result<Long> {
        val request =
            BottariTemplateCreateRequest(
                title = title,
                description = description,
                bottariTemplateItems = items,
                hashtagNames = hashtag,
            )
        return bottariTemplateRemoteDataSource.createBottariTemplate(request)
    }

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
