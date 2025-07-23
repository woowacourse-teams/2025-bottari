package com.bottari.data.repository

import com.bottari.data.model.bottari.ToggleAlarmStateRequest
import com.bottari.data.source.remote.AlarmRemoteDataSource
import com.bottari.domain.repository.AlarmRepository

class AlarmRepositoryImpl(
    private val alarmRemoteDataSource: AlarmRemoteDataSource,
) : AlarmRepository {
    override suspend fun toggleAlarm(
        ssaid: String,
        bottariId: Long,
        isActive: Boolean,
    ): Result<Boolean> =
        if (isActive) {
            alarmRemoteDataSource.toggleAlarmState(ToggleAlarmStateRequest(bottariId, ssaid, "active"))
        } else {
            alarmRemoteDataSource.toggleAlarmState(ToggleAlarmStateRequest(bottariId, ssaid, "inactive"))
        }
}
