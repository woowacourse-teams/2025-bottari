package com.bottari.domain.usecase.appConfig

import com.bottari.domain.model.exception.BottariResult
import com.bottari.domain.repository.AppConfigRepository

class GetPermissionFlagUseCase(
    private val repository: AppConfigRepository,
) {
    suspend operator fun invoke(): BottariResult<Boolean> = repository.getPermissionFlag()
}
