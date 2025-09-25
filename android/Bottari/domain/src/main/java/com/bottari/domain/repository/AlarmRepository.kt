package com.bottari.domain.repository

import com.bottari.domain.model.alarm.Alarm
import kotlinx.coroutines.flow.Flow

interface AlarmRepository {
    fun findAlarm(bottariId: Long): Flow<Alarm?>

    suspend fun saveAlarm(
        bottariId: Long,
        alarm: Alarm,
    ): Result<Unit>

    suspend fun updateAlarmActivate(
        id: Long,
        isActive: Boolean,
    ): Result<Unit>
}
