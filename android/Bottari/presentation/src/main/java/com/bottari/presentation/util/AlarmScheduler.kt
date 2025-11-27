package com.bottari.presentation.util

import android.app.AlarmManager
import android.app.AlarmManager.AlarmClockInfo
import android.app.PendingIntent
import android.content.Context
import com.bottari.core.domain.model.alarm.AlarmTriggerTimeCalculator
import com.bottari.core.domain.model.notification.Notification
import com.bottari.presentation.compose.edit.personal.ComposePersonalBottariEditActivity
import com.bottari.presentation.receiver.AlarmReceiver
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class AlarmScheduler @Inject constructor(
    @ApplicationContext private val context: Context,
    private val calculator: AlarmTriggerTimeCalculator,
) {
    private val manager: AlarmManager = context.getSystemService(AlarmManager::class.java)

    fun scheduleAlarm(notification: Notification) {
        val triggerTime = calculator.calculate(alarm = notification.alarm) ?: return
        scheduleAlarm(notification, triggerTime)
    }

    fun cancelAlarm(notification: Notification) {
        val pendingIntent = createPendingIntent(notification)
        manager.cancel(pendingIntent)
    }

    private fun scheduleAlarm(
        notification: Notification,
        triggerTime: Long,
    ) {
        val editPendingIntent = createEditPendingIntent(notification)
        val pendingIntent = createPendingIntent(notification)
        val alarmClockInfo = AlarmClockInfo(triggerTime, editPendingIntent)
        manager.setAlarmClock(alarmClockInfo, pendingIntent)
    }

    private fun createEditPendingIntent(notification: Notification): PendingIntent {
        val intent =
            ComposePersonalBottariEditActivity.newIntent(
                context,
                notification.bottariId,
                false,
            )
        return PendingIntent.getActivity(
            context,
            notification.bottariId.toInt(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE,
        )
    }

    private fun createPendingIntent(notification: Notification): PendingIntent =
        PendingIntent.getBroadcast(
            context,
            notification.bottariId.toInt(),
            AlarmReceiver.newIntent(context, notification),
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE,
        )
}
