package com.bottari.core.data.source.remote

interface RemoteConfigRemoteDataSource {
    suspend fun getMinUpdateVersionCode(): Result<Int>
}
