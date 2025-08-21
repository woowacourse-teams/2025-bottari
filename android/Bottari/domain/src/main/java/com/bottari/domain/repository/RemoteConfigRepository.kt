package com.bottari.domain.repository

interface RemoteConfigRepository {
    suspend fun getMinUpdateVersionCode(): Result<Int>
}
