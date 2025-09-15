package com.bottari.data.source.remote

import com.bottari.data.common.util.safeApiCall
import com.bottari.data.model.alarm.request.AlarmCreateRequest
import com.bottari.data.model.alarm.request.AlarmSaveRequest
import com.bottari.data.service.AlarmService

class AlarmRemoteDataSourceImpl(
    private val alarmService: AlarmService,
) : AlarmRemoteDataSource {
    override suspend fun saveAlarm(
        id: Long,
        alarmRequest: AlarmSaveRequest,
    ): Result<Unit> =
        safeApiCall {
            alarmService.saveAlarm(id = id, alarmSaveRequest = alarmRequest)
        }

    override suspend fun createAlarm(
        bottariId: Long,
        alarmRequest: AlarmCreateRequest,
    ): Result<Unit> =
        safeApiCall {
            alarmService.createAlarm(bottariId = bottariId, alarmCreateRequest = alarmRequest)
        }

    override suspend fun activeAlarmState(id: Long): Result<Unit> =
        safeApiCall {
            alarmService.activeAlarm(id = id)
        }

    override suspend fun inactiveAlarmState(id: Long): Result<Unit> =
        safeApiCall {
            alarmService.inactiveAlarm(id = id)
        }
}
