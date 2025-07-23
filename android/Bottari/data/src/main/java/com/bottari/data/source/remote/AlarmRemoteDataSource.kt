package com.bottari.data.source.remote

import com.bottari.data.model.bottari.ToggleAlarmStateRequest

interface AlarmRemoteDataSource {
    suspend fun toggleAlarmState(request: ToggleAlarmStateRequest): Result<Boolean>
}
