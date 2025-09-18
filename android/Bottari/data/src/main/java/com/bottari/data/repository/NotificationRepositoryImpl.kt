package com.bottari.data.repository

import com.bottari.data.model.local.notification.NotificationEntity
import com.bottari.data.source.remote.NotificationLocalDataSource
import com.bottari.domain.model.exception.BottariResult
import com.bottari.domain.model.exception.toBottariResult
import com.bottari.domain.model.notification.Notification
import com.bottari.domain.repository.NotificationRepository

class NotificationRepositoryImpl(
    private val dataSource: NotificationLocalDataSource,
) : NotificationRepository {
    override suspend fun getNotifications(): BottariResult<List<Notification>> =
        dataSource
            .getNotifications()
            .mapCatching { entities -> entities.map { entity -> entity.toDomain() } }
            .toBottariResult()

    override suspend fun saveNotification(vararg notification: Notification): BottariResult<Unit> =
        runCatching {
            val entities =
                notification
                    .map { notification -> NotificationEntity.fromDomain(notification) }
                    .toTypedArray()
            dataSource.saveNotification(*entities).getOrThrow()
        }.toBottariResult()

    override suspend fun deleteNotification(bottariId: Long): BottariResult<Unit> =
        dataSource.deleteNotification(bottariId).toBottariResult()
}
