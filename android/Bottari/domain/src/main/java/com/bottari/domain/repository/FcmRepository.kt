package com.bottari.domain.repository

import com.bottari.domain.model.exception.BottariResult

interface FcmRepository {
    suspend fun saveFcmToken(fcmToken: String): BottariResult<Unit>
}
