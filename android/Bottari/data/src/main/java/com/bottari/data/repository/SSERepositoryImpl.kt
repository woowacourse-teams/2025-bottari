package com.bottari.data.repository

import com.bottari.data.source.remote.SSERemoteDataSource
import com.bottari.domain.repository.SSERepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOf

class SSERepositoryImpl(
    private val sseRemoteDataSource: SSERemoteDataSource,
) : SSERepository {
    override suspend fun connectEvent(teamBottariId: Long): Flow<String> {
        val event = sseRemoteDataSource.connectEvent(teamBottariId).collect()
        return flowOf(event.toString())
    }
}
