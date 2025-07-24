package com.bottari.data.source.remote

import com.bottari.data.service.AlarmService
import com.bottari.data.util.safeApiCall

class AlarmRemoteDataSourceImpl(
    private val alarmService: AlarmService,
) : AlarmRemoteDataSource {

    override suspend fun toggleAlarmState(id: Long, ssaid: String, state: Boolean): Result<Boolean> =
        safeApiCall {
            val stateString = if (state) ALARM_ACTIVE else ALARM_INACTIVE
            alarmService.toggleAlarmState(id = id, ssaid = ssaid, state = stateString)
        }

    companion object {
        private const val ALARM_ACTIVE = "active"
        private const val ALARM_INACTIVE = "inactive"
    }
}
