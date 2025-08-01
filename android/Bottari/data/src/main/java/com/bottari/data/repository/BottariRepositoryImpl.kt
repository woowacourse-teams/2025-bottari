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
    override suspend fun fetchBottaries(ssaid: String): Result<List<Bottari>> =
        bottariRemoteDataSource.fetchBottaries(ssaid).mapCatching { bottari -> bottari.toDomain() }

    override suspend fun fetchBottariDetail(
        id: Long,
        ssaid: String,
    ): Result<BottariDetail> = bottariRemoteDataSource.fetchBottariDetail(id, ssaid).mapCatching { it.toDomain() }

    override suspend fun createBottari(
        ssaid: String,
        title: String,
    ): Result<Long?> = bottariRemoteDataSource.createBottari(ssaid, CreateBottariRequest(title))

    override suspend fun deleteBottari(
        id: Long,
        ssaid: String,
    ): Result<Unit> = bottariRemoteDataSource.deleteBottari(id, ssaid)

    override suspend fun saveBottariTitle(
        id: Long,
        ssaid: String,
        title: String,
    ): Result<Unit> = bottariRemoteDataSource.saveBottariTitle(id, ssaid, UpdateBottariTitleRequest(title))
}
