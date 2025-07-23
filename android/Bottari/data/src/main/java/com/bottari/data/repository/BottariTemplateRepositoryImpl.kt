package com.bottari.data.repository

import com.bottari.data.mapper.BottariTemplateMapper.toDomain
import com.bottari.data.source.remote.BottariTemplateRemoteDataSource
import com.bottari.domain.model.template.BottariTemplate
import com.bottari.domain.repository.BottariTemplateRepository

class BottariTemplateRepositoryImpl(
    private val bottariTemplateRemoteDataSource: BottariTemplateRemoteDataSource,
) : BottariTemplateRepository {
    override suspend fun fetchBottariTemplates(): Result<List<BottariTemplate>> =
        bottariTemplateRemoteDataSource
            .fetchBottariTemplates()
            .mapCatching { response -> response.map { it.toDomain() } }
}
