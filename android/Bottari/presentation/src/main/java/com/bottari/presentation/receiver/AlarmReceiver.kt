package com.bottari.presentation.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.bottari.logger.BottariLogger
import com.bottari.logger.model.UiEventType
import com.bottari.presentation.common.extension.getParcelableCompat
import com.bottari.presentation.model.NotificationUiModel
import com.bottari.presentation.util.AlarmScheduler
import com.bottari.presentation.util.NotificationHelper
import java.time.LocalDateTime

class AlarmReceiver : BroadcastReceiver() {
    private val scheduler: AlarmScheduler by lazy { AlarmScheduler() }
    private val notificationHelper: NotificationHelper by lazy { NotificationHelper() }

    override fun onReceive(
        context: Context?,
        intent: Intent,
    ) {
        val notification = intent.getParcelableCompat<NotificationUiModel>(EXTRA_NOTIFICATION)
        notificationHelper.sendNotification(notification.id, notification.title)
        scheduler.scheduleNextAlarm(notification)
        BottariLogger.ui(
            UiEventType.NOTIFICATION_CREATE,
            mapOf("notification_id" to notification.id, "time" to LocalDateTime.now().toString()),
        )
    }

    companion object {
        private const val EXTRA_NOTIFICATION = "EXTRA_NOTIFICATION"

        fun newIntent(
            context: Context,
            notification: NotificationUiModel,
        ): Intent =
            Intent(
                context,
                AlarmReceiver::class.java,
            ).apply {
                putExtra(EXTRA_NOTIFICATION, notification)
            }
    }
}
