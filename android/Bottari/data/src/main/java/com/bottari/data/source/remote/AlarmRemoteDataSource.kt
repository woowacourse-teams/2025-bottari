package com.bottari.data.source.remote

interface AlarmRemoteDataSource {
    suspend fun toggleAlarmState(id: Long, ssaid: String, state: Boolean): Result<Boolean>
}
