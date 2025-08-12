package com.bottari.data.repository

import com.bottari.data.mapper.BottariMapper.toDomain
import com.bottari.data.model.bottari.CreateBottariRequest
import com.bottari.data.model.bottari.UpdateBottariTitleRequest
import com.bottari.data.source.remote.BottariRemoteDataSource
import com.bottari.domain.model.bottari.Bottari
import com.bottari.domain.model.bottari.BottariDetail
import com.bottari.domain.repository.BottariRepository

class BottariRepositoryImpl(
    private val bottariRemoteDataSource: BottariRemoteDataSource,
) : BottariRepository {
    override suspend fun fetchBottaries(): Result<List<Bottari>> =
        bottariRemoteDataSource
            .fetchBottaries()
            .mapCatching { bottaries -> bottaries.map { it.toDomain() } }

    override suspend fun fetchBottariDetail(id: Long): Result<BottariDetail> =
        bottariRemoteDataSource.fetchBottariDetail(id).mapCatching { it.toDomain() }

    override suspend fun createBottari(title: String): Result<Long?> = bottariRemoteDataSource.createBottari(CreateBottariRequest(title))

    override suspend fun deleteBottari(id: Long): Result<Unit> = bottariRemoteDataSource.deleteBottari(id)

    override suspend fun saveBottariTitle(
        id: Long,
        title: String,
    ): Result<Unit> = bottariRemoteDataSource.saveBottariTitle(id, UpdateBottariTitleRequest(title))
}
