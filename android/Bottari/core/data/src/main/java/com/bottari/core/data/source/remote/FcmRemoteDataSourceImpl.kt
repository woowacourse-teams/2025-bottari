package com.bottari.core.data.source.remote

import com.bottari.core.data.common.safeApiCall
import com.bottari.core.network.dto.fcm.FcmTokenSaveRequest
import com.bottari.core.network.service.FcmService
import javax.inject.Inject

class FcmRemoteDataSourceImpl @Inject constructor(
    private val fcmService: FcmService,
) : FcmRemoteDataSource {
    override suspend fun saveFcmToken(request: FcmTokenSaveRequest): Result<Unit> = safeApiCall { fcmService.saveFcmToken(request) }
}
