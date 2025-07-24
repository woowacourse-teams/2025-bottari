package com.bottari.data.source.remote

import com.bottari.data.service.AlarmService
import com.bottari.data.util.safeApiCall

class AlarmRemoteDataSourceImpl(
    val alarmService: AlarmService,
) : AlarmRemoteDataSource {
    override suspend fun toggleAlarmState(id: Long, ssaid: String, state: String): Result<Boolean> =
        safeApiCall {
            alarmService.toggleAlarmState(id, ssaid, state)
        }
}
