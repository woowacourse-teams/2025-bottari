package com.bottari.data.repository

import com.bottari.data.model.bottari.BottariCreateRequest
import com.bottari.data.model.bottari.BottariTitleUpdateRequest
import com.bottari.data.source.remote.BottariRemoteDataSource
import com.bottari.domain.model.bottari.Bottari
import com.bottari.domain.model.bottari.BottariState
import com.bottari.domain.repository.BottariRepository

class BottariRepositoryImpl(
    private val bottariRemoteDataSource: BottariRemoteDataSource,
) : BottariRepository {
    override suspend fun fetchBottaries(): Result<List<BottariState>> =
        bottariRemoteDataSource
            .fetchBottaries()
            .mapCatching { bottaries -> bottaries.map { it.toDomain() } }

    override suspend fun fetchBottariDetail(id: Long): Result<Bottari> =
        bottariRemoteDataSource.fetchBottariDetail(id).mapCatching { it.toDomain() }

    override suspend fun createBottari(title: String): Result<Long?> =
        bottariRemoteDataSource.createBottari(
            BottariCreateRequest(title),
        )

    override suspend fun deleteBottari(id: Long): Result<Unit> = bottariRemoteDataSource.deleteBottari(id)

    override suspend fun saveBottariTitle(
        id: Long,
        title: String,
    ): Result<Unit> =
        bottariRemoteDataSource.saveBottariTitle(
            id,
            BottariTitleUpdateRequest(title),
        )
}
