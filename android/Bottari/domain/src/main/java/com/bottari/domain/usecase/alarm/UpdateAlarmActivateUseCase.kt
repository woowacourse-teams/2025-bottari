package com.bottari.domain.usecase.alarm

import com.bottari.domain.repository.AlarmRepository

class UpdateAlarmActivateUseCase(
    private val alarmRepository: AlarmRepository,
) {
    suspend operator fun invoke(
        bottariId: Long,
        isActive: Boolean,
    ): Result<Unit> = alarmRepository.updateAlarmActivate(bottariId, isActive)
}
