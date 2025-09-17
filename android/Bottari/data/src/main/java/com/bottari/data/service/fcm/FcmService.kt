package com.bottari.data.service.fcm

import com.bottari.data.model.remote.fcm.FcmTokenSaveRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.PATCH

interface FcmService {
    @PATCH("/fcm")
    suspend fun saveFcmToken(
        @Body request: FcmTokenSaveRequest,
    ): Response<Unit>
}
