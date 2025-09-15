package com.bottari.data.mapper.notification

import com.bottari.data.mapper.alarm.AlarmMapper.getDate
import com.bottari.data.mapper.alarm.AlarmMapper.getRepeatDays
import com.bottari.data.mapper.alarm.AlarmMapper.toAlarm
import com.bottari.data.mapper.alarm.AlarmMapper.toTypeString
import com.bottari.data.model.notification.NotificationEntity
import com.bottari.domain.model.notification.Notification

object NotificationMapper {
    fun Notification.toNotificationEntity(): NotificationEntity =
        NotificationEntity(
            bottariId = bottariId,
            bottariTitle = bottariTitle,
            alarmId = alarm.id,
            isActive = alarm.isActive,
            alarmType = alarm.alarmType.toTypeString(),
            time = alarm.time,
            date = alarm.alarmType.getDate(),
            repeatDays = alarm.alarmType.getRepeatDays(),
        )

    fun NotificationEntity.toNotification(): Notification =
        Notification(
            bottariId = bottariId,
            bottariTitle = bottariTitle,
            alarm = this.toAlarm(),
        )
}
