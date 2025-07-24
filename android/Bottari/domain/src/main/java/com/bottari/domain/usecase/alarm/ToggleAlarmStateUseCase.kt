package com.bottari.domain.usecase.alarm

import com.bottari.domain.repository.AlarmRepository

class ToggleAlarmStateUseCase(
    private val alarmRepository: AlarmRepository,
) {
    suspend operator fun invoke(
        ssaid: String,
        bottariId: Long,
        isActive: Boolean,
    ): Result<Boolean> {
        val state = if (isActive) STATE_ACTIVE else STATE_INACTIVE
        return alarmRepository.toggleAlarm(ssaid, bottariId, state)
    }

    companion object {
        private const val STATE_ACTIVE = "active"
        private const val STATE_INACTIVE = "inactive"
    }
}
