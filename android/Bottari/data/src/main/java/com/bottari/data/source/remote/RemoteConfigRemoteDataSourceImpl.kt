package com.bottari.data.source.remote

import com.bottari.data.remote.RemoteConfig
import javax.inject.Inject

class RemoteConfigRemoteDataSourceImpl @Inject constructor(
    private val remoteConfig: RemoteConfig,
) : RemoteConfigRemoteDataSource {
    override suspend fun getMinUpdateVersionCode(): Result<Int> =
        runCatching {
            remoteConfig.getMinUpdateVersionCode()
        }
}
