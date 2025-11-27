package com.bottari.core.network.remote

interface RemoteConfig {
    suspend fun getMinUpdateVersionCode(): Int
}
