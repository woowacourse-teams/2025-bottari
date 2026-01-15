package com.bottari.common.util

import com.bottari.core.domain.model.notification.Notification

interface AlarmScheduler {
    fun scheduleAlarm(notification: Notification)

    fun cancelAlarm(notification: Notification)
}
