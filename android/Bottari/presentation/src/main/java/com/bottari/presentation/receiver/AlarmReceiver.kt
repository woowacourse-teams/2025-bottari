package com.bottari.presentation.receiver

import android.content.Context
import android.content.Intent
import com.bottari.core.domain.model.notification.Notification
import com.bottari.logger.BottariLogger
import com.bottari.logger.model.UiEventType
import com.bottari.presentation.common.extension.getParcelableCompat
import com.bottari.presentation.model.alarm.NotificationUiModel
import com.bottari.presentation.util.AlarmScheduler
import com.bottari.presentation.util.NotificationHelper
import dagger.hilt.android.AndroidEntryPoint
import java.time.LocalDateTime
import javax.inject.Inject

@AndroidEntryPoint
class AlarmReceiver : HiltBroadcastReceiver() {
    @Inject
    lateinit var alarmScheduler: AlarmScheduler

    @Inject
    lateinit var notificationHelper: NotificationHelper

    override fun onReceive(
        context: Context?,
        intent: Intent?,
    ) {
        super.onReceive(context, intent)
        val notification = intent.getParcelableCompat<NotificationUiModel>(EXTRA_NOTIFICATION)
        notificationHelper.sendPersonalNotification(notification.bottariId, notification.bottariTitle)
        alarmScheduler.scheduleNextAlarm(notification = notification.toDomain())
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
