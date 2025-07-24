package com.bottari.data.source.remote

import com.bottari.data.model.bottari.AlarmRequest

interface AlarmRemoteDataSource {
    suspend fun saveAlarm(
        ssaid: String,
        id: Long,
        alarmRequest: AlarmRequest,
    ): Result<Unit>

    suspend fun createAlarm(
        ssaid: String,
        bottariId: Long,
        alarmRequest: AlarmRequest,
    ): Result<Unit>

    suspend fun activeAlarmState(
        id: Long,
        ssaid: String,
    ): Result<Unit>

    suspend fun inactiveAlarmState(
        id: Long,
        ssaid: String,
    ): Result<Unit>
}
