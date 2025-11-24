package com.bottari.core.domain.repository

import com.bottari.core.domain.model.bottari.personal.PersonalBottari
import com.bottari.core.domain.model.notification.Notification
import kotlinx.coroutines.flow.Flow

interface BottariRepository {
    fun fetchBottaries(): Flow<List<PersonalBottari>>

    suspend fun fetchNotifications(): Result<List<Notification>>

    fun findBottari(id: Long): Flow<PersonalBottari?>

    suspend fun createBottari(title: String): Result<Long>

    suspend fun createBottariWithItems(
        title: String,
        itemNames: List<String>,
    ): Result<Long>

    suspend fun deleteBottari(id: Long): Result<Unit>

    suspend fun saveBottariTitle(
        id: Long,
        title: String,
    ): Result<Unit>
}
