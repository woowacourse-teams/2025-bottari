package com.bottari.domain.usecase.alarm

import com.bottari.domain.model.alarm.Alarm
import com.bottari.domain.repository.AlarmRepository

class CreateAlarmUseCase(
    private val alarmRepository: AlarmRepository,
) {
    suspend operator fun invoke(
        ssaid: String,
        bottariId: Long,
        alarm: Alarm,
    ): Result<Unit> = alarmRepository.createAlarm(ssaid, bottariId, alarm)
}
