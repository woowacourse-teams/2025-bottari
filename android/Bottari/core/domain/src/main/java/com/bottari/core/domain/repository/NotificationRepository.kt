package com.bottari.core.domain.repository

import com.bottari.core.domain.model.notification.Notification

interface NotificationRepository {
    suspend fun getNotifications(): Result<List<Notification>>

    suspend fun saveNotification(vararg notification: Notification): Result<Unit>

    suspend fun deleteNotification(bottariId: Long): Result<Unit>
}
