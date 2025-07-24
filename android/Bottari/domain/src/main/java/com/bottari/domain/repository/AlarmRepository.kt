package com.bottari.domain.repository

interface AlarmRepository {
    suspend fun activeAlarm(
        ssaid: String,
        alarmId: Long,
    ): Result<Unit>

    suspend fun inactiveAlarm(
        ssaid: String,
        alarmId: Long,
    ): Result<Unit>
}
