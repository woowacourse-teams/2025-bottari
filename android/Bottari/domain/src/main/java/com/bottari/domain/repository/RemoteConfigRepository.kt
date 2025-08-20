package com.bottari.domain.repository

interface RemoteConfigRepository {
    suspend fun getMinVersionCode(): Result<Int>
}
