package com.bottari.domain.repository

import com.bottari.domain.model.exception.BottariResult
import com.bottari.domain.model.notification.Notification

interface NotificationRepository {
    suspend fun getNotifications(): BottariResult<List<Notification>>

    suspend fun saveNotification(vararg notification: Notification): BottariResult<Unit>

    suspend fun deleteNotification(bottariId: Long): BottariResult<Unit>
}
