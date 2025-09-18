package com.bottari.domain.usecase.fcm

import com.bottari.domain.model.exception.BottariResult
import com.bottari.domain.repository.FcmRepository

class SaveFcmTokenUseCase(
    private val fcmRepository: FcmRepository,
) {
    suspend operator fun invoke(fcmToken: String): BottariResult<Unit> = fcmRepository.saveFcmToken(fcmToken)
}
