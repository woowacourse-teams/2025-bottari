package com.bottari.data.source.remote

import com.bottari.data.common.util.safeApiCall
import com.bottari.data.model.remote.fcm.FcmTokenSaveRequest
import com.bottari.data.service.FcmService

class FcmRemoteDataSourceImpl(
    private val fcmService: FcmService,
) : FcmRemoteDataSource {
    override suspend fun saveFcmToken(request: FcmTokenSaveRequest): Result<Unit> = safeApiCall { fcmService.saveFcmToken(request) }
}
