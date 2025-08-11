package com.bottari.data.mapper

import com.bottari.data.model.notification.NotificationEntity
import com.bottari.domain.model.alarm.Alarm
import com.bottari.domain.model.alarm.AlarmType
import com.bottari.domain.model.notification.Notification
import java.time.LocalDate

object NotificationMapper {
    private const val NON_REPEAT = "NON_REPEAT"
    private const val REPEAT = "REPEAT"
    private const val ERROR_UNKNOWN_ALARM_TYPE = "지원하지 않는 알람 유형입니다: %s"

    fun Notification.toEntity(): NotificationEntity =
        NotificationEntity(
            bottariId = bottariId,
            bottariTitle = bottariTitle,
            alarmId = alarm.id!!,
            isActive = alarm.isActive,
            alarmType = alarm.alarmType.toTypeString(),
            time = alarm.time,
            date = alarm.alarmType.getDate(),
            repeatDays = alarm.alarmType.getRepeatDays(),
        )

    fun NotificationEntity.toDomain(): Notification =
        Notification(
            bottariId = bottariId,
            bottariTitle = bottariTitle,
            alarm = this.toAlarmDomain(),
        )

    private fun AlarmType.toTypeString(): String =
        when (this) {
            is AlarmType.NonRepeat -> NON_REPEAT
            is AlarmType.Repeat -> REPEAT
        }

    private fun AlarmType.getDate(): LocalDate? =
        when (this) {
            is AlarmType.NonRepeat -> date
            is AlarmType.Repeat -> null
        }

    private fun AlarmType.getRepeatDays(): List<Int> =
        when (this) {
            is AlarmType.NonRepeat -> emptyList()
            is AlarmType.Repeat -> repeatDays
        }

    private fun NotificationEntity.toAlarmDomain(): Alarm =
        Alarm(
            id = alarmId,
            isActive = isActive,
            time = time,
            alarmType = this.toTypeDomain(),
            location = null,
        )

    private fun NotificationEntity.toTypeDomain(): AlarmType =
        when (this.alarmType) {
            NON_REPEAT -> AlarmType.NonRepeat(date!!)
            REPEAT -> AlarmType.Repeat(repeatDays)
            else -> throw IllegalArgumentException(ERROR_UNKNOWN_ALARM_TYPE)
        }
}
