package com.bottari.domain.repository

import com.bottari.domain.model.bottari.Bottari
import kotlinx.coroutines.flow.Flow

interface BottariRepository {
    fun fetchBottaries(): Flow<List<Bottari>>

    fun fetchBottari(id: Long): Flow<Bottari>

    suspend fun createBottari(title: String): Result<Unit>

    suspend fun deleteBottari(id: Long): Result<Unit>

    suspend fun saveBottariTitle(
        id: Long,
        title: String,
    ): Result<Unit>
}
