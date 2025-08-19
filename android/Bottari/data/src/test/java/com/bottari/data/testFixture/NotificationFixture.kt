package com.bottari.data.testFixture

import com.bottari.data.mapper.NotificationMapper.toEntity
import com.bottari.data.model.notification.NotificationEntity
import com.bottari.domain.model.alarm.Alarm
import com.bottari.domain.model.alarm.AlarmType
import com.bottari.domain.model.notification.Notification
import java.time.LocalDate
import java.time.LocalTime

val NOTIFICATION_FIXTURE =
    Notification(
        bottariId = 1L,
        bottariTitle = "Test",
        alarm =
            Alarm(
                id = null,
                isActive = true,
                time = LocalTime.now(),
                alarmType = AlarmType.NonRepeat(LocalDate.now()),
                location = null,
            ),
    )

val NOTIFICATION_ENTITY_FIXTURE = NOTIFICATION_FIXTURE.toEntity()

val NOTIFICATIONS_FIXTURE = List(3) { NOTIFICATION_FIXTURE }

val NOTIFICATION_ENTITIES_FIXTURE = List(3) { NOTIFICATION_ENTITY_FIXTURE }
