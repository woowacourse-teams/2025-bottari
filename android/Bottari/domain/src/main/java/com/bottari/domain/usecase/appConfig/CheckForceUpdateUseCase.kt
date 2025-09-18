package com.bottari.domain.usecase.appConfig

import com.bottari.domain.model.exception.BottariResult
import com.bottari.domain.model.exception.map
import com.bottari.domain.repository.RemoteConfigRepository

class CheckForceUpdateUseCase(
    private val remoteConfigRepository: RemoteConfigRepository,
) {
    suspend operator fun invoke(currentVersionCode: Int): BottariResult<Boolean> =
        remoteConfigRepository
            .getMinUpdateVersionCode()
            .map { minVersionCode -> minVersionCode > currentVersionCode }
}
