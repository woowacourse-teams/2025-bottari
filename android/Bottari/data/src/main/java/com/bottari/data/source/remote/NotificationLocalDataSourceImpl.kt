package com.bottari.data.source.remote

import com.bottari.data.local.notification.NotificationDao
import com.bottari.data.model.notification.NotificationEntity

class NotificationLocalDataSourceImpl(
    private val dao: NotificationDao,
) : NotificationLocalDataSource {
    override suspend fun getNotifications(): Result<List<NotificationEntity>> = runCatching { dao.getNotifications() }

    override suspend fun saveNotification(vararg notification: NotificationEntity): Result<Unit> =
        runCatching {
            dao.saveNotification(*notification)
        }

    override suspend fun deleteNotification(bottariId: Long): Result<Unit> =
        runCatching {
            dao.deleteNotification(bottariId)
        }
}
