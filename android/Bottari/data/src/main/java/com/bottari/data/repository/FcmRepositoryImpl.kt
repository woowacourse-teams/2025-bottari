package com.bottari.data.repository

import com.bottari.data.model.remote.fcm.FcmTokenSaveRequest
import com.bottari.data.source.local.MemberIdentifierLocalDataSource
import com.bottari.data.source.remote.FcmRemoteDataSource
import com.bottari.domain.extension.flatMapCatching
import com.bottari.domain.repository.FcmRepository
import javax.inject.Inject

class FcmRepositoryImpl @Inject constructor(
    private val fcmRemoteDataSource: FcmRemoteDataSource,
    private val memberIdentifierLocalDataSource: MemberIdentifierLocalDataSource,
) : FcmRepository {
    override suspend fun saveFcmToken(fcmToken: String): Result<Unit> =
        memberIdentifierLocalDataSource
            .getMemberId()
            .flatMapCatching {
                fcmRemoteDataSource.saveFcmToken(FcmTokenSaveRequest(fcmToken))
            }
}
