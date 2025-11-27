package com.bottari.core.domain.usecase.notification

import com.bottari.core.domain.model.notification.Notification
import com.bottari.core.domain.repository.BottariRepository
import javax.inject.Inject

class FetchNotificationsUseCase @Inject constructor(
    private val repository: BottariRepository,
) {
    suspend operator fun invoke(): Result<List<Notification>> = repository.fetchNotifications()
}
