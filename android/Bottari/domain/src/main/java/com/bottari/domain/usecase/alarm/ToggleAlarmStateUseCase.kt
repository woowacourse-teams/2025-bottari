package com.bottari.domain.usecase.alarm

import com.bottari.domain.repository.AlarmRepository

class ToggleAlarmStateUseCase(
    private val alarmRepository: AlarmRepository,
) {
    suspend operator fun invoke(
        ssaid: String,
        bottariId: Long,
        isActive: Boolean,
    ) = alarmRepository.toggleAlarm(ssaid, bottariId, isActive)
}
