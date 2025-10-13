package com.bottari.presentation.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.bottari.domain.model.notification.Notification
import com.bottari.logger.BottariLogger
import com.bottari.logger.model.UiEventType
import com.bottari.presentation.common.extension.getParcelableCompat
import com.bottari.presentation.model.alarm.NotificationUiModel
import com.bottari.presentation.util.AlarmScheduler.scheduleNextAlarm
import com.bottari.presentation.util.NotificationHelper
import java.time.LocalDateTime

class AlarmReceiver : BroadcastReceiver() {
    private val notificationHelper: NotificationHelper by lazy { NotificationHelper() }

    override fun onReceive(
        context: Context?,
        intent: Intent,
    ) {
        val notification = intent.getParcelableCompat<NotificationUiModel>(EXTRA_NOTIFICATION)
        notificationHelper.sendPersonalNotification(notification.bottariId, notification.bottariTitle)
        scheduleNextAlarm(notification = notification.toDomain())
        BottariLogger.ui(
            UiEventType.NOTIFICATION_CREATE,
            mapOf("notification_id" to notification.bottariId, "time" to LocalDateTime.now().toString()),
        )
    }

    companion object {
        private const val EXTRA_NOTIFICATION = "EXTRA_NOTIFICATION"

        fun newIntent(
            context: Context,
            notification: Notification,
        ): Intent =
            Intent(
                context,
                AlarmReceiver::class.java,
            ).apply {
                putExtra(EXTRA_NOTIFICATION, NotificationUiModel.fromDomain(notification))
            }
    }
}
