package com.bottari.data.repository

import com.bottari.data.source.remote.AlarmRemoteDataSource
import com.bottari.domain.repository.AlarmRepository

class AlarmRepositoryImpl(
    private val alarmRemoteDataSource: AlarmRemoteDataSource,
) : AlarmRepository {
    override suspend fun toggleAlarm(
        ssaid: String,
        bottariId: Long,
        state: String,
    ): Result<Boolean> = alarmRemoteDataSource.toggleAlarmState(id = bottariId, ssaid = ssaid, state = state)
}
