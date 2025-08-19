package com.bottari.data.source.remote

import com.bottari.data.model.notification.NotificationEntity

interface NotificationLocalDataSource {
    suspend fun getNotifications(): Result<List<NotificationEntity>>

    suspend fun saveNotification(vararg notification: NotificationEntity): Result<Unit>

    suspend fun deleteNotification(bottariId: Long): Result<Unit>
}
