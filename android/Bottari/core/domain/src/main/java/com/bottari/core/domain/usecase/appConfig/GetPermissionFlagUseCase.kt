package com.bottari.core.domain.usecase.appConfig

import com.bottari.core.domain.repository.AppConfigRepository
import javax.inject.Inject

class GetPermissionFlagUseCase @Inject constructor(
    private val repository: AppConfigRepository,
) {
    suspend operator fun invoke(): Result<Boolean> = repository.getPermissionFlag()
}
