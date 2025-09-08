package com.bottari.data.source.remote

import com.bottari.data.model.bottari.CreateAlarmRequest
import com.bottari.data.model.bottari.SaveAlarmRequest

interface AlarmRemoteDataSource {
    suspend fun saveAlarm(
        id: Long,
        alarmRequest: SaveAlarmRequest,
    ): Result<Unit>

    suspend fun createAlarm(
        bottariId: Long,
        alarmRequest: CreateAlarmRequest,
    ): Result<Unit>

    suspend fun activeAlarmState(id: Long): Result<Unit>

    suspend fun inactiveAlarmState(id: Long): Result<Unit>
}
