package com.bottari.data.source.remote

import com.bottari.data.model.remote.alarm.AlarmCreateRequest
import com.bottari.data.model.remote.alarm.AlarmSaveRequest
import com.bottari.data.service.AlarmService
import com.bottari.domain.model.exception.BottariResult

class AlarmRemoteDataSourceImpl(
    private val alarmService: AlarmService,
) : AlarmRemoteDataSource {
    override suspend fun saveAlarm(
        id: Long,
        alarmRequest: AlarmSaveRequest,
    ): BottariResult<Unit> = alarmService.saveAlarm(id = id, alarmSaveRequest = alarmRequest)

    override suspend fun createAlarm(
        bottariId: Long,
        alarmRequest: AlarmCreateRequest,
    ): BottariResult<Long> = alarmService.createAlarm(bottariId = bottariId, alarmCreateRequest = alarmRequest)

    override suspend fun activeAlarmState(id: Long): BottariResult<Unit> = alarmService.activeAlarm(id = id)

    override suspend fun inactiveAlarmState(id: Long): BottariResult<Unit> = alarmService.inactiveAlarm(id = id)
}
