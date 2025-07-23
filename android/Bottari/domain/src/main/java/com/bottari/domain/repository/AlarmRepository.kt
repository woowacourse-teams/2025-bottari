package com.bottari.domain.repository

interface AlarmRepository {
    suspend fun toggleAlarm(
        ssaid: String,
        bottariId: Long,
        isActive: Boolean,
    ): Result<Boolean>
}
