package com.bottari.domain.usecase.appConfig

import com.bottari.domain.model.exception.BottariResult
import com.bottari.domain.repository.AppConfigRepository

class SavePermissionFlagUseCase(
    private val repository: AppConfigRepository,
) {
    suspend operator fun invoke(flag: Boolean): BottariResult<Unit> = repository.savePermissionFlag(flag)
}
