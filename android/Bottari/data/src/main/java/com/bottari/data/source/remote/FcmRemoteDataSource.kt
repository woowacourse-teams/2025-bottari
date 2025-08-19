package com.bottari.data.source.remote

import com.bottari.data.model.fcm.SaveFcmTokenRequest

interface FcmRemoteDataSource {
    suspend fun saveFcmToken(request: SaveFcmTokenRequest): Result<Unit>
}
