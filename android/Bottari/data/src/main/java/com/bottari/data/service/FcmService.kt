package com.bottari.data.service

import com.bottari.data.model.fcm.SaveFcmTokenRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.PATCH

interface FcmService {
    @PATCH("/fcm")
    suspend fun saveFcmToken(
        @Body request: SaveFcmTokenRequest,
    ): Response<Unit>
}
