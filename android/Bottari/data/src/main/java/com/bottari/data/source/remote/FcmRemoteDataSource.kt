package com.bottari.data.source.remote

import com.bottari.data.model.fcm.FcmTokenSaveRequest

interface FcmRemoteDataSource {
    suspend fun saveFcmToken(request: FcmTokenSaveRequest): Result<Unit>
}
