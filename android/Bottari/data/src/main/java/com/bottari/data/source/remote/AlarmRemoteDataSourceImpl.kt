package com.bottari.data.source.remote

import com.bottari.data.model.bottari.ToggleAlarmStateRequest
import com.bottari.data.service.AlarmService
import com.bottari.data.util.safeApiCall

class AlarmRemoteDataSourceImpl(
    val alarmService: AlarmService,
) : AlarmRemoteDataSource {
    override suspend fun toggleAlarmState(request: ToggleAlarmStateRequest): Result<Boolean> =
        safeApiCall {
            alarmService.toggleAlarmState(request.id, request.active, request.ssaid)
        }
}
