package com.bottari.core.domain.repository

interface RemoteConfigRepository {
    suspend fun getMinUpdateVersionCode(): Result<Int>
}
