package com.bottari.data.source.remote

interface RemoteConfigRemoteDataSource {
    suspend fun getMinUpdateVersionCode(): Result<Int>
}
