package com.bottari.domain.usecase.notification

import com.bottari.domain.model.notification.Notification
import com.bottari.domain.repository.NotificationRepository

class SaveNotificationsUseCase(
    private val repository: NotificationRepository,
) {
    suspend operator fun invoke(vararg notification: Notification): Result<Unit> =
        runCatching { repository.saveNotification(*notification) }
}
