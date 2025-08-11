package com.bottari.domain.repository

import com.bottari.domain.model.alarm.Alarm

interface AlarmRepository {
    suspend fun saveAlarm(
        id: Long,
        alarm: Alarm,
    ): Result<Unit>

    suspend fun createAlarm(
        bottariId: Long,
        alarm: Alarm,
    ): Result<Unit>

    suspend fun activeAlarm(alarmId: Long): Result<Unit>

    suspend fun inactiveAlarm(alarmId: Long): Result<Unit>
}
