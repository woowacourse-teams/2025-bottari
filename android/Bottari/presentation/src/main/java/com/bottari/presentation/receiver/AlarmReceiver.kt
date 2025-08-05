package com.bottari.presentation.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.bottari.presentation.common.extension.getParcelableCompat
import com.bottari.presentation.model.AlarmTypeUiModel
import com.bottari.presentation.model.DayOfWeekUiModel
import com.bottari.presentation.model.NotificationUiModel
import com.bottari.presentation.util.AlarmScheduler
import com.bottari.presentation.util.NotificationHelper
import java.time.DayOfWeek
import java.time.LocalDate

class AlarmReceiver : BroadcastReceiver() {
    private val scheduler: AlarmScheduler by lazy { AlarmScheduler() }
    private val notificationHelper: NotificationHelper by lazy { NotificationHelper() }

    override fun onReceive(
        context: Context?,
        intent: Intent,
    ) {
        val notification = intent.getParcelableCompat<NotificationUiModel>(EXTRA_NOTIFICATION)
        notificationHelper.sendNotification(notification.id, notification.title)
        scheduleNextAlarm(notification)
    }

    private fun scheduleNextAlarm(notification: NotificationUiModel) {
        if (notification.alarm.type == AlarmTypeUiModel.NON_REPEAT) return
        val availableDays = calculateAvailableDays(notification.alarm.daysOfWeek)
        val today = LocalDate.now()
        if (availableDays.contains(today.dayOfWeek)) {
            val nextAlarmDate = today.plusWeeks(WEEKS_TO_ADD)
            scheduler.scheduleAlarm(notification.copy(alarm = notification.alarm.copy(date = nextAlarmDate)))
        }
    }

    private fun calculateAvailableDays(daysOfWeek: List<DayOfWeekUiModel>): List<DayOfWeek> =
        daysOfWeek
            .filter { dayOfWeek -> dayOfWeek.isChecked }
            .map { it.dayOfWeek }

    companion object {
        private const val EXTRA_NOTIFICATION = "EXTRA_NOTIFICATION"
        private const val WEEKS_TO_ADD = 1L

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
