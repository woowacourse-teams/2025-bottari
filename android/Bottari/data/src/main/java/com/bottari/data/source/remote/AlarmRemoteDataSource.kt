package com.bottari.data.source.remote

import com.bottari.data.model.alarm.request.AlarmCreateRequest
import com.bottari.data.model.alarm.request.AlarmSaveRequest

interface AlarmRemoteDataSource {
    suspend fun saveAlarm(
        id: Long,
        alarmRequest: AlarmSaveRequest,
    ): Result<Unit>

    suspend fun createAlarm(
        bottariId: Long,
        alarmRequest: AlarmCreateRequest,
    ): Result<Unit>

    suspend fun activeAlarmState(id: Long): Result<Unit>

    suspend fun inactiveAlarmState(id: Long): Result<Unit>
}
