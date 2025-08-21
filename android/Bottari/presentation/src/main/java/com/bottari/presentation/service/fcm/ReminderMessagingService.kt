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
    private val notificationHelper by lazy { NotificationHelper() }
    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        logData("New FCM token received: $token")

        serviceScope.launch {
            saveFcmTokenUseCase(token).onFailure { logError("Failed to save FCM token", it) }
        }
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        val data = message.data
        val from = message.from.orEmpty()
        val title = message.notification?.title.orEmpty()

        logData("Message received from: $from, title: $title, data: $data")

        if (data.isEmpty()) return

        sendFcmNotification(data, from)
    }

    override fun handleIntent(intent: Intent?) {
        val sanitizedIntent =
            intent?.apply {
                extras
                    ?.apply {
                        remove(Constants.MessageNotificationKeys.ENABLE_NOTIFICATION)
                        remove(OLD_NOTIFICATION_PREFIX)
                    }?.also { replaceExtras(it) }
            }
        super.handleIntent(sanitizedIntent)
    }

    override fun onDestroy() {
        super.onDestroy()
        serviceScope.coroutineContext.cancel()
    }

    private fun sendFcmNotification(
        data: Map<String, String>,
        from: String,
    ) {
        FcmMessage.fromData(data).sendNotification(notificationHelper)
        logUi("Notification sent for message from: $from with data: $data")
    }

    private fun logData(message: String) {
        BottariLogger.data(message = formatLog(message))
    }

    private fun logUi(message: String) {
        BottariLogger.ui(
            eventName = formatLog("Send Notification"),
            params = mapOf("message" to message),
        )
    }

    private fun logError(
        message: String,
        error: Throwable,
    ) {
        BottariLogger.error(
            message = formatLog("$message: ${error.message}"),
            throwable = error,
        )
    }

    private fun formatLog(message: String) = "[FCM] $message"

    companion object {
        private const val OLD_NOTIFICATION_PREFIX = "gcm.notification"
    }
}
