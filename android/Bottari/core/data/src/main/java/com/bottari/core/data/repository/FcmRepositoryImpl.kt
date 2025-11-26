package com.bottari.core.data.repository

import com.bottari.core.data.source.local.MemberIdentifierLocalDataSource
import com.bottari.core.data.source.remote.FcmRemoteDataSource
import com.bottari.core.domain.extension.flatMapCatching
import com.bottari.core.domain.repository.FcmRepository
import com.bottari.core.network.dto.fcm.FcmTokenSaveRequest
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
