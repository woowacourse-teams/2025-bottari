package com.bottari.domain.usecase.alarm

import com.bottari.domain.repository.AlarmRepository
import javax.inject.Inject

class UpdateAlarmActivateUseCase @Inject constructor(
    private val alarmRepository: AlarmRepository,
) {
    suspend operator fun invoke(
        bottariId: Long,
        isActive: Boolean,
    ): Result<Unit> = alarmRepository.updateAlarmActivate(bottariId, isActive)
}
