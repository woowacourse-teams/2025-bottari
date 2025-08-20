package com.bottari.domain.repository

import kotlinx.coroutines.flow.Flow

interface SSERepository {
    suspend fun connectEvent(teamBottariId: Long): Flow<String>
}
