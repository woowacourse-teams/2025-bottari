package com.bottari.core.data.fixture

import com.bottari.core.domain.model.alarm.Alarm
import com.bottari.core.domain.model.alarm.AlarmType
import com.bottari.core.domain.model.notification.Notification
import com.bottari.core.local.entity.notification.NotificationEntity
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

val NOTIFICATION_ENTITY_FIXTURE = NotificationEntity.fromDomain(NOTIFICATION_FIXTURE)

val NOTIFICATION_ENTITIES_FIXTURE = List(3) { NOTIFICATION_ENTITY_FIXTURE }
