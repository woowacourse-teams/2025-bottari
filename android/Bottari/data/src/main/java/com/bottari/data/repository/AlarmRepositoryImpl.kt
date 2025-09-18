package com.bottari.data.repository

import com.bottari.data.model.remote.alarm.AlarmCreateRequest
import com.bottari.data.model.remote.alarm.AlarmSaveRequest
import com.bottari.data.source.remote.AlarmRemoteDataSource
import com.bottari.domain.model.alarm.Alarm
import com.bottari.domain.model.exception.BottariResult
import com.bottari.domain.repository.AlarmRepository

class AlarmRepositoryImpl(
    private val alarmRemoteDataSource: AlarmRemoteDataSource,
) : AlarmRepository {
    override suspend fun saveAlarm(
        id: Long,
        alarm: Alarm,
    ): BottariResult<Unit> = alarmRemoteDataSource.saveAlarm(id, AlarmSaveRequest.fromDomain(alarm))

    override suspend fun createAlarm(
        bottariId: Long,
        alarm: Alarm,
    ): BottariResult<Long> = alarmRemoteDataSource.createAlarm(bottariId, AlarmCreateRequest.fromDomain(alarm))

    override suspend fun activeAlarm(alarmId: Long): BottariResult<Unit> = alarmRemoteDataSource.activeAlarmState(id = alarmId)

    override suspend fun inactiveAlarm(alarmId: Long): BottariResult<Unit> = alarmRemoteDataSource.inactiveAlarmState(id = alarmId)
}
