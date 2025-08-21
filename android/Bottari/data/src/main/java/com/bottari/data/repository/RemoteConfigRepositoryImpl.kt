package com.bottari.data.repository

import com.bottari.data.source.remote.RemoteConfigRemoteDataSource
import com.bottari.domain.repository.RemoteConfigRepository

class RemoteConfigRepositoryImpl(
    private val remoteConfigRemoteDataSource: RemoteConfigRemoteDataSource,
) : RemoteConfigRepository {
    override suspend fun getMinUpdateVersionCode(): Result<Int> = remoteConfigRemoteDataSource.getMinUpdateVersionCode()
}
