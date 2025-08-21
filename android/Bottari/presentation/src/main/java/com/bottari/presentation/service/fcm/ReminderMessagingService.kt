package com.bottari.presentation.service.fcm

import android.content.Intent
import com.bottari.di.UseCaseProvider
import com.bottari.domain.usecase.fcm.SaveFcmTokenUseCase
import com.bottari.logger.BottariLogger
import com.bottari.presentation.util.NotificationHelper
import com.google.firebase.messaging.Constants
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

class ReminderMessagingService(
    private val saveFcmTokenUseCase: SaveFcmTokenUseCase = UseCaseProvider.saveFcmTokenUseCase,
) : FirebaseMessagingService() {
    private val notificationHelper: NotificationHelper by lazy { NotificationHelper() }
    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        serviceScope.launch {
            saveFcmTokenUseCase(token)
                .onFailure { error ->
                    BottariLogger.error(LOG_FORMAT.format(error.message), error)
                }
        }
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        BottariLogger.data(LOG_FORMAT.format(message.data))
        if (message.data.isEmpty()) return

        FcmMessage
            .fromData(message.data)
            .sendNotification(notificationHelper)
    }

    override fun handleIntent(intent: Intent?) {
        val sanitizedIntent =
            intent?.apply {
                extras
                    ?.apply {
                        remove(Constants.MessageNotificationKeys.ENABLE_NOTIFICATION)
                        remove(NOTIFICATION_PREFIX_OLD)
                    }?.also { replaceExtras(it) }
            }
        super.handleIntent(sanitizedIntent)
    }

    override fun onDestroy() {
        super.onDestroy()
        serviceScope.coroutineContext.cancel()
    }

    companion object {
        private const val LOG_FORMAT = "[FCM] %s"
        private const val NOTIFICATION_PREFIX_OLD = "gcm.notification"
    }
}
