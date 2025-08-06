package com.bottari.presentation.util

import android.app.AlarmManager
import android.app.AlarmManager.AlarmClockInfo
import android.app.PendingIntent
import android.content.Context
import com.bottari.di.ApplicationContextProvider
import com.bottari.presentation.model.AlarmTypeUiModel
import com.bottari.presentation.model.DayOfWeekUiModel
import com.bottari.presentation.model.NotificationUiModel
import com.bottari.presentation.receiver.AlarmReceiver
import com.bottari.presentation.view.edit.personal.PersonalBottariEditActivity
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.time.temporal.TemporalAdjusters

class AlarmScheduler(
    private val context: Context = ApplicationContextProvider.applicationContext,
) {
    private val manager: AlarmManager = context.getSystemService(AlarmManager::class.java)

    fun scheduleAlarm(notification: NotificationUiModel) {
        if (notification.alarm.type == AlarmTypeUiModel.NON_REPEAT) {
            scheduleNonRepeatAlarm(notification)
            return
        }
        scheduleRepeatAlarm(notification)
    }

    fun scheduleNextWeekAlarm(notification: NotificationUiModel) {
        val alarm = notification.alarm
        if (alarm.type == AlarmTypeUiModel.NON_REPEAT) return
        val availableDays = getAvailableDays(alarm.daysOfWeek)
        val today = LocalDate.now()
        if (availableDays.contains(today.dayOfWeek)) {
            val nextAlarmDate = today.plusWeeks(WEEKS_TO_ADD)
            scheduleAlarm(notification.copy(alarm = alarm.copy(date = nextAlarmDate)))
        }
    }

    fun cancelAlarm(notification: NotificationUiModel) {
        if (notification.alarm.type == AlarmTypeUiModel.NON_REPEAT) {
            cancelNonRepeatAlarm(notification)
            return
        }
        cancelRepeatAlarm(notification)
    }

    private fun scheduleRepeatAlarm(notification: NotificationUiModel) {
        handleRepeatDays(notification) { dayOfWeek ->
            val requestCode = generateRequestCode(notification.id, dayOfWeek)
            val triggerTime = getNextTriggerTime(dayOfWeek, notification.alarm.time)
            scheduleAlarmInternal(notification, requestCode, triggerTime)
        }
    }

    private fun scheduleNonRepeatAlarm(notification: NotificationUiModel) {
        val requestCode = generateRequestCode(notification.id)
        val triggerTime =
            LocalDateTime.of(notification.alarm.date, notification.alarm.time).toTimeMillis()
        scheduleAlarmInternal(notification, requestCode, triggerTime)
    }

    private fun getAvailableDays(daysOfWeek: List<DayOfWeekUiModel>): List<DayOfWeek> =
        daysOfWeek
            .filter { dayOfWeekUiModel -> dayOfWeekUiModel.isChecked }
            .map { daysOfWeekUiModel -> daysOfWeekUiModel.dayOfWeek }

    private fun cancelRepeatAlarm(notification: NotificationUiModel) {
        handleRepeatDays(notification) { dayOfWeek ->
            val requestCode = generateRequestCode(notification.id, dayOfWeek)
            cancelAlarmInternal(notification, requestCode)
        }
    }

    private fun cancelNonRepeatAlarm(notification: NotificationUiModel) {
        val requestCode = generateRequestCode(notification.id)
        cancelAlarmInternal(notification, requestCode)
    }

    private fun handleRepeatDays(
        notification: NotificationUiModel,
        action: (DayOfWeek) -> Unit,
    ) {
        notification.alarm.daysOfWeek
            .filter { dayOfWeekUiModel -> dayOfWeekUiModel.isChecked }
            .forEach { dayOfWeekUiModel -> action(dayOfWeekUiModel.dayOfWeek) }
    }

    private fun generateRequestCode(
        bottariId: Long,
        dayOfWeek: DayOfWeek? = null,
    ): Int {
        val dayOffset = dayOfWeek?.ordinal ?: 0
        return (bottariId * DAY_OF_WEEK_MULTIPLIER + dayOffset).toInt()
    }

    private fun getNextTriggerTime(
        dayOfWeek: DayOfWeek,
        time: LocalTime,
    ): Long {
        val now = LocalDateTime.now()
        var nextTriggerTime =
            now
                .with(TemporalAdjusters.nextOrSame(dayOfWeek))
                .withHour(time.hour)
                .withMinute(time.minute)
                .withSecond(0)
                .withNano(0)

        if (nextTriggerTime.isBefore(now)) nextTriggerTime = nextTriggerTime.plusWeeks(WEEKS_TO_ADD)
        return nextTriggerTime.toTimeMillis()
    }

    private fun scheduleAlarmInternal(
        notification: NotificationUiModel,
        requestCode: Int,
        triggerTime: Long,
    ) {
        val editPendingIntent = createEditPendingIntent(notification, requestCode)
        val pendingIntent = createPendingIntent(notification, requestCode)
        val alarmClockInfo = AlarmClockInfo(triggerTime, editPendingIntent)
        manager.setAlarmClock(alarmClockInfo, pendingIntent)
    }

    private fun cancelAlarmInternal(
        notification: NotificationUiModel,
        requestCode: Int,
    ) {
        val pendingIntent = createPendingIntent(notification, requestCode)
        manager.cancel(pendingIntent)
    }

    private fun createEditPendingIntent(
        notification: NotificationUiModel,
        requestCode: Int,
    ): PendingIntent {
        val intent =
            PersonalBottariEditActivity.newIntent(
                context,
                notification.id,
                false,
            )
        return PendingIntent.getActivity(
            context,
            requestCode,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE,
        )
    }

    private fun createPendingIntent(
        notification: NotificationUiModel,
        requestCode: Int,
    ): PendingIntent =
        PendingIntent.getBroadcast(
            context,
            requestCode,
            AlarmReceiver.newIntent(context, notification),
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE,
        )

    private fun LocalDateTime.toTimeMillis(): Long =
        this
            .atZone(ZoneId.systemDefault())
            .toInstant()
            .toEpochMilli()

    companion object {
        private const val DAY_OF_WEEK_MULTIPLIER = 10
        private const val WEEKS_TO_ADD = 1L
    }
}
