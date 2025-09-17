package com.bottari.data.source.remote

import com.bottari.data.model.remote.fcm.FcmTokenSaveRequest
import com.bottari.data.service.FcmService
import com.bottari.domain.model.exception.BottariResult

class FcmRemoteDataSourceImpl(
    private val fcmService: FcmService,
) : FcmRemoteDataSource {
    override suspend fun saveFcmToken(request: FcmTokenSaveRequest): BottariResult<Unit> = fcmService.saveFcmToken(request)
}
