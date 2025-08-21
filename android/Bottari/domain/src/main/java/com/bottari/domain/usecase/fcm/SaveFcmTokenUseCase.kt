package com.bottari.domain.usecase.fcm

import com.bottari.domain.repository.FcmRepository

class SaveFcmTokenUseCase(
    private val fcmRepository: FcmRepository,
) {
    suspend operator fun invoke(fcmToken: String): Result<Unit> = fcmRepository.saveFcmToken(fcmToken)
}
