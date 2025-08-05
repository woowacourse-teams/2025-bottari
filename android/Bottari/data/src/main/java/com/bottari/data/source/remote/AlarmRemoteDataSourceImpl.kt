package com.bottari.data.source.remote

import com.bottari.data.common.util.safeApiCall
import com.bottari.data.model.bottari.AlarmRequest
import com.bottari.data.service.AlarmService

class AlarmRemoteDataSourceImpl(
    private val alarmService: AlarmService,
) : AlarmRemoteDataSource {
    override suspend fun saveAlarm(
        ssaid: String,
        id: Long,
        alarmRequest: AlarmRequest,
    ): Result<Unit> =
        safeApiCall {
            alarmService.saveAlarm(ssaid = ssaid, id = id, alarmRequest = alarmRequest)
        }

    override suspend fun createAlarm(
        ssaid: String,
        bottariId: Long,
        alarmRequest: AlarmRequest,
    ): Result<Unit> =
        safeApiCall {
            alarmService.createAlarm(ssaid = ssaid, bottariId = bottariId, alarmRequest = alarmRequest)
        }

    override suspend fun activeAlarmState(
        id: Long,
        ssaid: String,
    ): Result<Unit> =
        safeApiCall {
            alarmService.activeAlarm(id = id, ssaid = ssaid)
        }

    override suspend fun inactiveAlarmState(
        id: Long,
        ssaid: String,
    ): Result<Unit> =
        safeApiCall {
            alarmService.inactiveAlarm(id = id, ssaid = ssaid)
        }
}
