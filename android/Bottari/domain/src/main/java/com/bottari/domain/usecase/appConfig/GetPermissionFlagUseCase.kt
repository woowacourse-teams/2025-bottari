package com.bottari.domain.usecase.appConfig

import com.bottari.domain.repository.AppConfigRepository

class GetPermissionFlagUseCase(
    private val repository: AppConfigRepository,
) {
    suspend operator fun invoke(): Result<Boolean> = repository.getPermissionFlag()
}
