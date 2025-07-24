package com.bottari.data.repository

import com.bottari.data.source.remote.AlarmRemoteDataSource
import com.bottari.domain.repository.AlarmRepository

class AlarmRepositoryImpl(
    private val alarmRemoteDataSource: AlarmRemoteDataSource,
) : AlarmRepository {
    override suspend fun activeAlarm(
        ssaid: String,
        alarmId: Long,
    ): Result<Unit> = alarmRemoteDataSource.activeAlarmState(id = alarmId, ssaid = ssaid)

    override suspend fun inactiveAlarm(
        ssaid: String,
        alarmId: Long,
    ): Result<Unit> = alarmRemoteDataSource.inactiveAlarmState(id = alarmId, ssaid = ssaid)
}
