package com.bottari.domain.repository

import com.bottari.domain.model.bottari.personal.PersonalBottari
import kotlinx.coroutines.flow.Flow

interface BottariRepository {
    fun fetchBottaries(): Flow<List<PersonalBottari>>

    fun fetchBottari(id: Long): Flow<PersonalBottari>

    suspend fun saveBottari(title: String): Result<Long>

    suspend fun deleteBottari(id: Long): Result<Unit>

    suspend fun saveBottariTitle(
        id: Long,
        title: String,
    ): Result<Unit>
}
