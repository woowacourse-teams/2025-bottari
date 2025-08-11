package com.bottari.data.repository

import com.bottari.data.mapper.NotificationMapper.toDomain
import com.bottari.data.mapper.NotificationMapper.toEntity
import com.bottari.data.source.remote.NotificationLocalDataSource
import com.bottari.domain.model.notification.Notification
import com.bottari.domain.repository.NotificationRepository

class NotificationRepositoryImpl(
    private val dataSource: NotificationLocalDataSource,
) : NotificationRepository {
    override suspend fun getNotifications(): Result<List<Notification>> =
        dataSource
            .getNotifications()
            .mapCatching { entities -> entities.map { entity -> entity.toDomain() } }

    override suspend fun saveNotification(vararg notification: Notification): Result<Unit> =
        dataSource.saveNotification(
            *notification
                .map { notification -> notification.toEntity() }
                .toTypedArray(),
        )

    override suspend fun deleteNotification(bottariId: Long): Result<Unit> = dataSource.deleteNotification(bottariId)
}
