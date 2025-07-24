package com.bottari.data.source.remote

interface AlarmRemoteDataSource {
    suspend fun activeAlarmState(
        id: Long,
        ssaid: String,
    ): Result<Unit>

    suspend fun inactiveAlarmState(
        id: Long,
        ssaid: String,
    ): Result<Unit>
}
