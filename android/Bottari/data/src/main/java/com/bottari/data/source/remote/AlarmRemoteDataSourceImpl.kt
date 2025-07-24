package com.bottari.data.source.remote

import com.bottari.data.service.AlarmService
import com.bottari.data.util.safeApiCall

class AlarmRemoteDataSourceImpl(
    val alarmService: AlarmService,
) : AlarmRemoteDataSource {
    override suspend fun toggleAlarmState(id: Long, ssaid: String, state: Boolean): Result<Boolean> =
        safeApiCall {
            if(state){
                alarmService.toggleAlarmState(id = id, ssaid = ssaid, state = "active")
            }
            else{
                alarmService.toggleAlarmState(id = id, ssaid = ssaid, state =  "inactive")
            }
        }
}
