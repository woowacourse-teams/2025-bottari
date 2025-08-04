package com.bottari.domain.usecase.appConfig

import com.bottari.domain.repository.AppConfigRepository

class SavePermissionFlagUseCase(
    private val repository: AppConfigRepository,
) {
    suspend operator fun invoke(flag: Boolean): Result<Unit> = repository.savePermissionFlag(flag)
}
