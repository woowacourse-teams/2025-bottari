package com.bottari.data.remote

interface RemoteConfig {
    suspend fun getMinUpdateVersionCode(): Int
}
