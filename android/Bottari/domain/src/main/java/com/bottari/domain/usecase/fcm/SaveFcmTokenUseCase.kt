package com.bottari.domain.usecase.fcm

import com.bottari.domain.repository.FcmRepository
import javax.inject.Inject

class SaveFcmTokenUseCase @Inject constructor(
    private val fcmRepository: FcmRepository,
) {
    suspend operator fun invoke(fcmToken: String): Result<Unit> = fcmRepository.saveFcmToken(fcmToken)
}
