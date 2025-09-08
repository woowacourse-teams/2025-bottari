package com.bottari.data.source.remote

import com.bottari.data.common.util.safeApiCall
import com.bottari.data.model.alarm.CreateAlarmRequest
import com.bottari.data.model.alarm.SaveAlarmRequest
import com.bottari.data.service.AlarmService

class AlarmRemoteDataSourceImpl(
    private val alarmService: AlarmService,
) : AlarmRemoteDataSource {
    override suspend fun saveAlarm(
        id: Long,
        alarmRequest: SaveAlarmRequest,
    ): Result<Unit> =
        safeApiCall {
            alarmService.saveAlarm(id = id, saveAlarmRequest = alarmRequest)
        }

    override suspend fun createAlarm(
        bottariId: Long,
        alarmRequest: CreateAlarmRequest,
    ): Result<Unit> =
        safeApiCall {
            alarmService.createAlarm(bottariId = bottariId, createAlarmRequest = alarmRequest)
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
