package com.bottari.domain.usecase.alarm

import com.bottari.domain.model.alarm.Alarm
import com.bottari.domain.repository.AlarmRepository

class SaveAlarmUseCase(
    private val alarmRepository: AlarmRepository,
) {
    suspend operator fun invoke(
        ssaid: String,
        id: Long,
        alarm: Alarm,
    ): Result<Unit> = alarmRepository.saveAlarm(ssaid, id, alarm)
}
