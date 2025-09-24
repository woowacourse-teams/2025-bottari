package com.bottari.domain.usecase.alarm

import com.bottari.domain.model.alarm.Alarm
import com.bottari.domain.repository.AlarmRepository
import kotlinx.coroutines.flow.Flow

class FetchAlarmUseCase(
    private val alarmRepository: AlarmRepository,
) {
    operator fun invoke(bottariId: Long): Flow<Alarm?> = alarmRepository.fetchAlarm(bottariId)
}
