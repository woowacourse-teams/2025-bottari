package com.bottari.presentation.util

import android.app.AlarmManager
import android.app.AlarmManager.AlarmClockInfo
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.bottari.di.ApplicationContextProvider
import com.bottari.presentation.model.NotificationUiModel
import com.bottari.presentation.receiver.AlarmReceiver
import com.bottari.presentation.view.edit.personal.PersonalBottariEditActivity
import java.time.LocalDateTime
import java.time.ZoneId

class AlarmScheduler(
    private val context: Context = ApplicationContextProvider.applicationContext,
) {
    private val manager: AlarmManager = context.getSystemService(AlarmManager::class.java)

    fun scheduleAlarm(notification: NotificationUiModel) {
        val editPendingIntent = createEditPendingIntent(notification)
        val pendingIntent = createPendingIntent(notification)
        val triggerTime =
            LocalDateTime.of(notification.alarm.date, notification.alarm.time).toTimeMillis()
        val alarmClockInfo = AlarmClockInfo(triggerTime, editPendingIntent)
        manager.setAlarmClock(alarmClockInfo, pendingIntent)
    }

    fun cancelAlarm(notification: NotificationUiModel) {
        val intent = Intent(context, AlarmReceiver::class.java)
        val pendingIntent =
            PendingIntent.getBroadcast(
                context,
                notification.id.toInt(),
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE,
            )
        manager.cancel(pendingIntent)
    }

    private fun createEditPendingIntent(notification: NotificationUiModel): PendingIntent {
        val intent =
            PersonalBottariEditActivity.newIntent(
                context,
                notification.id,
                false,
            )
        return PendingIntent.getActivity(
            context,
            notification.id.toInt(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE,
        )
    }

    private fun createPendingIntent(notification: NotificationUiModel): PendingIntent =
        PendingIntent.getBroadcast(
            context,
            notification.id.toInt(),
            AlarmReceiver.newIntent(context, notification),
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE,
        )

    private fun LocalDateTime.toTimeMillis(): Long =
        this
            .atZone(ZoneId.systemDefault())
            .toInstant()
            .toEpochMilli()
}
