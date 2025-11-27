package com.bottari.core.domain.repository

interface FcmRepository {
    suspend fun saveFcmToken(fcmToken: String): Result<Unit>
}
