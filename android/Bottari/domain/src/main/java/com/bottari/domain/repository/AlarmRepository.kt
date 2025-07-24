package com.bottari.domain.repository

interface AlarmRepository {
    suspend fun toggleAlarm(
        ssaid: String,
        bottariId: Long,
        state: String,
    ): Result<Boolean>
}
