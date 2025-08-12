package com.bottari.data.source.remote

import com.bottari.data.model.bottari.AlarmRequest

interface AlarmRemoteDataSource {
    suspend fun saveAlarm(
        id: Long,
        alarmRequest: AlarmRequest,
    ): Result<Unit>

    suspend fun createAlarm(
        bottariId: Long,
        alarmRequest: AlarmRequest,
    ): Result<Unit>

    suspend fun activeAlarmState(id: Long): Result<Unit>

    suspend fun inactiveAlarmState(id: Long): Result<Unit>
}
