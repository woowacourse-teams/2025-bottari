package com.bottari.domain.usecase.appConfig

import com.bottari.domain.repository.RemoteConfigRepository

class CheckForceUpdateUseCase(
    private val remoteConfigRepository: RemoteConfigRepository,
) {
    suspend operator fun invoke(currentVersionCode: Int): Result<Boolean> =
        remoteConfigRepository
            .getMinUpdateVersionCode()
            .map { minVersionCode -> minVersionCode > currentVersionCode }
}
