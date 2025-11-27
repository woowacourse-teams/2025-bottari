package com.bottari.core.domain.usecase.alarm

import com.bottari.core.domain.model.alarm.Alarm
import com.bottari.core.domain.repository.AlarmRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class FindAlarmUseCase @Inject constructor(
    private val alarmRepository: AlarmRepository,
) {
    operator fun invoke(bottariId: Long): Flow<Alarm?> = alarmRepository.findAlarm(bottariId)
}
