package com.bottari.core.domain.usecase.alarm

import com.bottari.core.domain.model.alarm.Alarm
import com.bottari.core.domain.repository.AlarmRepository
import javax.inject.Inject

class SaveAlarmUseCase @Inject constructor(
    private val alarmRepository: AlarmRepository,
) {
    suspend operator fun invoke(
        bottariId: Long,
        bottariTitle: String,
        alarm: Alarm,
    ): Result<Unit> = alarmRepository.saveAlarm(bottariId, alarm)
}
