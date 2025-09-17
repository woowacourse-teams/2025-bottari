package com.bottari.data.model.local.notification

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.bottari.domain.model.alarm.Alarm
import com.bottari.domain.model.alarm.AlarmType
import com.bottari.domain.model.notification.Notification
import java.time.LocalDate
import java.time.LocalTime

private const val REPEAT = "REPEAT"
private const val NON_REPEAT = "NON_REPEAT"
private const val ERROR_UNKNOWN_ALARM_TYPE = "지원하지 않는 알람 유형입니다."

@Entity("notifications")
data class NotificationEntity(
    @PrimaryKey val bottariId: Long,
    val bottariTitle: String,
    val alarmId: Long?,
    val isActive: Boolean,
    val alarmType: String,
    val time: LocalTime,
    val date: LocalDate?,
    val repeatDays: List<Int>,
) {
    fun toDomain(): Notification =
        Notification(
            bottariId = bottariId,
            bottariTitle = bottariTitle,
            alarm = this.toAlarm(),
        )

    private fun toAlarm(): Alarm =
        Alarm(
            id = alarmId,
            isActive = isActive,
            time = time,
            alarmType = toAlarmType(),
            location = null,
        )

    private fun toAlarmType(): AlarmType =
        when (alarmType) {
            NON_REPEAT -> AlarmType.NonRepeat(date!!)
            REPEAT -> AlarmType.Repeat(repeatDays)
            else -> throw IllegalArgumentException(ERROR_UNKNOWN_ALARM_TYPE)
        }

    companion object {
        fun fromDomain(notification: Notification): NotificationEntity =
            NotificationEntity(
                bottariId = notification.bottariId,
                bottariTitle = notification.bottariTitle,
                alarmId = notification.alarm.id,
                isActive = notification.alarm.isActive,
                alarmType = notification.alarm.alarmType.toTypeString(),
                time = notification.alarm.time,
                date = notification.alarm.alarmType.getAlarmDate(),
                repeatDays = notification.alarm.alarmType.getAlarmRepeatDays(),
            )
    }
}
