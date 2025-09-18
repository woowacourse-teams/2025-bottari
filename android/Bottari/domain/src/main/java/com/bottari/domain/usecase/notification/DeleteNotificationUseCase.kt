package com.bottari.domain.usecase.notification

import com.bottari.domain.model.exception.BottariResult
import com.bottari.domain.repository.NotificationRepository

class DeleteNotificationUseCase(
    private val repository: NotificationRepository,
) {
    suspend operator fun invoke(bottariId: Long): BottariResult<Unit> = repository.deleteNotification(bottariId)
}
