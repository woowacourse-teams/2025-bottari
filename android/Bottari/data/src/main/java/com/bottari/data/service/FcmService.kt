package com.bottari.data.service

import com.bottari.data.model.remote.fcm.FcmTokenSaveRequest
import com.bottari.domain.model.exception.BottariResult
import retrofit2.http.Body
import retrofit2.http.PATCH

interface FcmService {
    @PATCH("/fcm")
    suspend fun saveFcmToken(
        @Body request: FcmTokenSaveRequest,
    ): BottariResult<Unit>
}
