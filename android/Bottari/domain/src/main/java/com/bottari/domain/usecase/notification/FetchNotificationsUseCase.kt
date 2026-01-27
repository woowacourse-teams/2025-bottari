package com.bottari.domain.usecase.notification

import com.bottari.domain.model.notification.Notification
import com.bottari.domain.repository.BottariRepository
import javax.inject.Inject

class FetchNotificationsUseCase @Inject constructor(
    private val repository: BottariRepository,
) {
    suspend operator fun invoke(): Result<List<Notification>> = repository.fetchNotifications()
}
