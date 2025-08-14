package com.bottari.presentation.fcm

import com.bottari.di.UseCaseProvider
import com.bottari.domain.usecase.fcm.SaveFcmTokenUseCase
import com.bottari.logger.BottariLogger
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ReminderMessagingService(
    private val saveFcmTokenUseCase: SaveFcmTokenUseCase = UseCaseProvider.saveFcmTokenUseCase,
) : FirebaseMessagingService() {
    override fun onNewToken(token: String) {
        super.onNewToken(token)
        CoroutineScope(Dispatchers.IO).launch {
            saveFcmTokenUseCase(token)
                .onFailure { error -> BottariLogger.error(error.message, error) }
        }
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
    }
}
