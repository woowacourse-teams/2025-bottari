package com.bottari.data.repository

import com.bottari.data.mapper.BottariDetailMapper.toDomain
import com.bottari.data.source.remote.BottariDetailRemoteDataSource
import com.bottari.domain.model.bottari.BottariDetail
import com.bottari.domain.repository.BottariDetailRepository

class BottariDetailRepositoryImpl(
    private val bottariDetailRemoteDataSource: BottariDetailRemoteDataSource,
) : BottariDetailRepository {
    override suspend fun findBottariDetail(
        id: Long,
        ssaid: String,
    ): Result<BottariDetail> =
        try {
            val response = bottariDetailRemoteDataSource.findBottari(id, ssaid)
            response.map { it.toDomain() }
        } catch (e: Exception) {
            Result.failure(e)
        }
}
