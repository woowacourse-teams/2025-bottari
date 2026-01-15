package com.bottari.bottari.util

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.AlarmManager.AlarmClockInfo
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.bottari.bottari.receiver.AlarmReceiver
import com.bottari.core.domain.model.alarm.AlarmTriggerTimeCalculator
import com.bottari.core.domain.model.notification.Notification
import com.bottari.feature.main.ComposeMainActivity
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import com.bottari.common.util.AlarmScheduler as AlarmSchedulerContract

class AlarmScheduler @Inject constructor(
    @param:ApplicationContext private val context: Context,
    private val calculator: AlarmTriggerTimeCalculator,
) : AlarmSchedulerContract {
    private val manager: AlarmManager = context.getSystemService(AlarmManager::class.java)

    override fun scheduleAlarm(notification: Notification) {
        val triggerTime = calculator.calculate(alarm = notification.alarm) ?: return
        scheduleAlarm(notification, triggerTime)
    }

    override fun cancelAlarm(notification: Notification) {
        val pendingIntent = createPendingIntent(notification)
        manager.cancel(pendingIntent)
    }

    @SuppressLint("ScheduleExactAlarm")
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
        // 이거 어디로 가는거지
//        val intent =
//            ComposeMainActivity.newIntentForPersonalEdit(
//                context = context,
//                bottariId = notification.bottariId,
//            )
        return PendingIntent.getActivity(
            context,
            notification.bottariId.toInt(),
            Intent(context, ComposeMainActivity::class.java),
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
