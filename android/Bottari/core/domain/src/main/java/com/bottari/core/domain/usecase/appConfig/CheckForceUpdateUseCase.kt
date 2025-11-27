package com.bottari.core.domain.usecase.appConfig

import com.bottari.core.domain.repository.RemoteConfigRepository
import javax.inject.Inject

class CheckForceUpdateUseCase @Inject constructor(
    private val remoteConfigRepository: RemoteConfigRepository,
) {
    suspend operator fun invoke(currentVersionCode: Int): Result<Boolean> =
        remoteConfigRepository
            .getMinUpdateVersionCode()
            .map { minVersionCode -> minVersionCode > currentVersionCode }
}
