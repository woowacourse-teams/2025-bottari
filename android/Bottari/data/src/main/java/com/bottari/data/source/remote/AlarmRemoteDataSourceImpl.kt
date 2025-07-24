package com.bottari.data.source.remote

import com.bottari.data.service.AlarmService
import com.bottari.data.util.safeApiCall

class AlarmRemoteDataSourceImpl(
    val alarmService: AlarmService,
) : AlarmRemoteDataSource {
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
