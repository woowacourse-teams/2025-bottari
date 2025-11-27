package com.bottari.core.data.source.remote

import com.bottari.core.network.dto.fcm.FcmTokenSaveRequest

interface FcmRemoteDataSource {
    suspend fun saveFcmToken(request: FcmTokenSaveRequest): Result<Unit>
}
