package com.bottari.data.source.remote

import com.bottari.data.common.util.safeApiCall
import com.bottari.data.model.bottari.AlarmRequest
import com.bottari.data.service.AlarmService

class AlarmRemoteDataSourceImpl(
    private val alarmService: AlarmService,
) : AlarmRemoteDataSource {
    override suspend fun saveAlarm(
        id: Long,
        alarmRequest: AlarmRequest,
    ): Result<Unit> =
        safeApiCall {
            alarmService.saveAlarm(id = id, alarmRequest = alarmRequest)
        }

    override suspend fun createAlarm(
        bottariId: Long,
        alarmRequest: AlarmRequest,
    ): Result<Unit> =
        safeApiCall {
            alarmService.createAlarm(bottariId = bottariId, alarmRequest = alarmRequest)
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
