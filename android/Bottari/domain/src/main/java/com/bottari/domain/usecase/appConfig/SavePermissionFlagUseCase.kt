package com.bottari.domain.usecase.appConfig

import com.bottari.domain.repository.AppConfigRepository
import javax.inject.Inject

class SavePermissionFlagUseCase @Inject constructor(
    private val repository: AppConfigRepository,
) {
    suspend operator fun invoke(flag: Boolean): Result<Unit> = repository.savePermissionFlag(flag)
}
