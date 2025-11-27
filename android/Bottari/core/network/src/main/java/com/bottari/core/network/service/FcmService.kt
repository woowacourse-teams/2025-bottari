package com.bottari.core.network.service

import com.bottari.core.network.dto.fcm.FcmTokenSaveRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.PATCH

interface FcmService {
    @PATCH("/fcm")
    suspend fun saveFcmToken(
        @Body request: FcmTokenSaveRequest,
    ): Response<Unit>
}
