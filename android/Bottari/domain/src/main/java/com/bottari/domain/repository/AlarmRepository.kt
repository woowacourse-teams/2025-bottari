package com.bottari.domain.repository

import com.bottari.domain.model.alarm.Alarm

interface AlarmRepository {
    suspend fun saveAlarm(
        ssaid: String,
        id: Long,
        alarm: Alarm,
    ): Result<Unit>

    suspend fun createAlarm(
        ssaid: String,
        bottariId: Long,
        alarm: Alarm,
    ): Result<Unit>

    suspend fun activeAlarm(
        ssaid: String,
        alarmId: Long,
    ): Result<Unit>

    suspend fun inactiveAlarm(
        ssaid: String,
        alarmId: Long,
    ): Result<Unit>
}
