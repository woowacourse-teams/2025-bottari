package com.bottari.core.data.source.remote

import com.bottari.core.network.remote.RemoteConfig
import javax.inject.Inject

class RemoteConfigRemoteDataSourceImpl @Inject constructor(
    private val remoteConfig: RemoteConfig,
) : RemoteConfigRemoteDataSource {
    override suspend fun getMinUpdateVersionCode(): Result<Int> =
        runCatching {
            remoteConfig.getMinUpdateVersionCode()
        }
}
