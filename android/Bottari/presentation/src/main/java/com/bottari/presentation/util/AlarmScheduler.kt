package com.bottari.presentation.util

import android.app.AlarmManager
import android.app.AlarmManager.AlarmClockInfo
import android.app.PendingIntent
import android.content.Context
import com.bottari.domain.model.alarm.AlarmType
import com.bottari.domain.model.notification.Notification
import com.bottari.presentation.compose.edit.personal.ComposePersonalBottariEditActivity
import com.bottari.presentation.receiver.AlarmReceiver
import dagger.hilt.android.qualifiers.ApplicationContext
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import javax.inject.Inject

class AlarmScheduler @Inject constructor(
    @ApplicationContext private val context: Context,
) {
    private val manager: AlarmManager = context.getSystemService(AlarmManager::class.java)

    fun scheduleAlarm(notification: Notification) {
        if (notification.alarm.isActive.not()) return

        if (notification.alarm.alarmType is AlarmType.NonRepeat) {
            scheduleNonRepeatAlarm(notification)
            return
        }
        scheduleRepeatAlarm(notification)
    }

    fun scheduleNextAlarm(notification: Notification) {
        if (notification.alarm.isActive.not()) return

        val alarm = notification.alarm
        if (alarm.alarmType is AlarmType.NonRepeat) return
        val triggerTime = getNextTriggerTime(notification = notification)
        scheduleAlarmInternal(notification, triggerTime)
    }

    fun cancelAlarm(notification: Notification) {
        val pendingIntent = createPendingIntent(notification)
        manager.cancel(pendingIntent)
    }

    private fun scheduleRepeatAlarm(notification: Notification) {
        val triggerTime = getNextTriggerTime(notification = notification)
        scheduleAlarmInternal(notification, triggerTime)
    }

    private fun scheduleNonRepeatAlarm(
        notification: Notification,
        now: LocalDateTime = LocalDateTime.now(),
    ) {
        val alarm = notification.alarm
        val alarmType = alarm.alarmType as AlarmType.NonRepeat
        val alarmDateTime = LocalDateTime.of(alarmType.date, alarm.time)
        if (alarmDateTime.isBefore(now)) return
        val triggerTime =
            LocalDateTime.of(alarmType.date, notification.alarm.time).toTimeMillis()
        scheduleAlarmInternal(notification, triggerTime)
    }

    private fun getNextTriggerTime(
        today: LocalDate = LocalDate.now(),
        nowTime: LocalTime = LocalTime.now(),
        notification: Notification,
    ): Long {
        val alarm = notification.alarm
        val alarmType = alarm.alarmType as AlarmType.Repeat
        val availableDays = alarmType.repeatDays.map(DayOfWeek::of)
        if (availableDays.contains(today.dayOfWeek) && nowTime.isBefore(alarm.time)) {
            return LocalDateTime.of(today, alarm.time).toTimeMillis()
        }
        val triggerDate =
            availableDays
                .map { dayOfWeek ->
                    val daysUntil =
                        (dayOfWeek.value - today.dayOfWeek.value + DAYS_IN_WEEK) % DAYS_IN_WEEK
                    val adjustedDaysUntil = if (daysUntil == 0) DAYS_IN_WEEK else daysUntil
                    today.plusDays(adjustedDaysUntil.toLong())
                }.minByOrNull { it.toEpochDay() }
        return LocalDateTime.of(triggerDate, alarm.time).toTimeMillis()
    }

    private fun scheduleAlarmInternal(
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

    private fun LocalDateTime.toTimeMillis(): Long =
        this
            .atZone(ZoneId.systemDefault())
            .toInstant()
            .toEpochMilli()

    companion object {
        private const val DAYS_IN_WEEK = 7
    }
}
