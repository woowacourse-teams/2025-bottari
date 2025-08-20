package com.bottari.data.source.remote

import com.bottari.data.remote.RemoteConfig

class RemoteConfigRemoteDataSourceImpl(
    private val remoteConfig: RemoteConfig,
) : RemoteConfigRemoteDataSource {
    override suspend fun getMinUpdateVersionCode(): Result<Int> =
        runCatching {
            remoteConfig.getMinUpdateVersionCode()
        }
}
