package com.bottari.domain.usecase.notification

import com.bottari.domain.repository.NotificationRepository

class DeleteNotificationUseCase(
    private val repository: NotificationRepository,
) {
    suspend operator fun invoke(bottariId: Long): Result<Unit> = repository.deleteNotification(bottariId)
}
