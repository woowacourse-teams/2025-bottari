package com.bottari.domain.repository

interface FcmRepository {
    suspend fun saveFcmToken(fcmToken: String): Result<Unit>
}
