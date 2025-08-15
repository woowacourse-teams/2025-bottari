package com.bottari.data.source.remote

import com.bottari.data.common.util.safeApiCall
import com.bottari.data.model.fcm.SaveFcmTokenRequest
import com.bottari.data.service.FcmService

class FcmRemoteDataSourceImpl(
    private val fcmService: FcmService,
) : FcmRemoteDataSource {
    override suspend fun saveFcmToken(request: SaveFcmTokenRequest): Result<Unit> = safeApiCall { fcmService.saveFcmToken(request) }
}
