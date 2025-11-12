package com.bottari.data.repository

import com.bottari.data.source.remote.RemoteConfigRemoteDataSource
import com.bottari.domain.repository.RemoteConfigRepository
import javax.inject.Inject

class RemoteConfigRepositoryImpl @Inject constructor(
    private val remoteConfigRemoteDataSource: RemoteConfigRemoteDataSource,
) : RemoteConfigRepository {
    override suspend fun getMinUpdateVersionCode(): Result<Int> = remoteConfigRemoteDataSource.getMinUpdateVersionCode()
}
