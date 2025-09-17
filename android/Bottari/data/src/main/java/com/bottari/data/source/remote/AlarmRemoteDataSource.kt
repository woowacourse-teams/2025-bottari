package com.bottari.data.source.remote

import com.bottari.data.model.remote.alarm.AlarmCreateRequest
import com.bottari.data.model.remote.alarm.AlarmSaveRequest
import com.bottari.domain.model.exception.BottariResult

interface AlarmRemoteDataSource {
    suspend fun saveAlarm(
        id: Long,
        alarmRequest: AlarmSaveRequest,
    ): BottariResult<Unit>

    suspend fun createAlarm(
        bottariId: Long,
        alarmRequest: AlarmCreateRequest,
    ): BottariResult<Long>

    suspend fun activeAlarmState(id: Long): BottariResult<Unit>

    suspend fun inactiveAlarmState(id: Long): BottariResult<Unit>
}
