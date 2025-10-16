package com.bottari.domain.usecase.alarm

import com.bottari.domain.model.alarm.Alarm
import com.bottari.domain.repository.AlarmRepository
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
