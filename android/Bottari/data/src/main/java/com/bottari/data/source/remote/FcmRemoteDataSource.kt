package com.bottari.data.source.remote

import com.bottari.data.model.remote.fcm.FcmTokenSaveRequest
import com.bottari.domain.model.exception.BottariResult

interface FcmRemoteDataSource {
    suspend fun saveFcmToken(request: FcmTokenSaveRequest): BottariResult<Unit>
}
