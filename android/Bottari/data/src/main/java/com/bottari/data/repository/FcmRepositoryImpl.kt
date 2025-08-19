package com.bottari.data.repository

import com.bottari.data.model.fcm.SaveFcmTokenRequest
import com.bottari.data.source.remote.FcmRemoteDataSource
import com.bottari.domain.repository.FcmRepository

class FcmRepositoryImpl(
    private val fcmRemoteDataSource: FcmRemoteDataSource,
) : FcmRepository {
    override suspend fun saveFcmToken(fcmToken: String): Result<Unit> = fcmRemoteDataSource.saveFcmToken(SaveFcmTokenRequest(fcmToken))
}
