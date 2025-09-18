package com.bottari.data.repository

import com.bottari.data.model.remote.fcm.FcmTokenSaveRequest
import com.bottari.data.source.local.MemberIdentifierLocalDataSource
import com.bottari.data.source.remote.FcmRemoteDataSource
import com.bottari.domain.extension.mapCatching
import com.bottari.domain.model.exception.BottariResult
import com.bottari.domain.repository.FcmRepository

class FcmRepositoryImpl(
    private val fcmRemoteDataSource: FcmRemoteDataSource,
    private val memberIdentifierLocalDataSource: MemberIdentifierLocalDataSource,
) : FcmRepository {
    override suspend fun saveFcmToken(fcmToken: String): BottariResult<Unit> =
        memberIdentifierLocalDataSource
            .getMemberId()
            .mapCatching {
                fcmRemoteDataSource.saveFcmToken(FcmTokenSaveRequest(fcmToken))
            }.getOrElse { throwable -> BottariResult.NetworkError(throwable) }
}
