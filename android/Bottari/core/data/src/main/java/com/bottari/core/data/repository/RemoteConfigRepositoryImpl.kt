package com.bottari.core.data.repository

import com.bottari.core.data.source.remote.RemoteConfigRemoteDataSource
import com.bottari.core.domain.repository.RemoteConfigRepository
import javax.inject.Inject

class RemoteConfigRepositoryImpl @Inject constructor(
    private val remoteConfigRemoteDataSource: RemoteConfigRemoteDataSource,
) : RemoteConfigRepository {
    override suspend fun getMinUpdateVersionCode(): Result<Int> = remoteConfigRemoteDataSource.getMinUpdateVersionCode()
}
