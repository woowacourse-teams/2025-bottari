package com.bottari.domain.usecase.notification

import com.bottari.domain.model.notification.Notification
import com.bottari.domain.repository.NotificationRepository

class GetNotificationsUseCase(
    private val repository: NotificationRepository,
) {
    suspend operator fun invoke(): Result<List<Notification>> = repository.getNotifications()
}
