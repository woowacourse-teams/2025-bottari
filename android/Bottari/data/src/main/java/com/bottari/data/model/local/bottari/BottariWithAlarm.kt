package com.bottari.data.model.local.bottari

import androidx.room.Embedded
import androidx.room.Relation
import com.bottari.domain.model.notification.Notification

data class BottariWithAlarm(
    @Embedded
    val bottari: BottariEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "bottariId",
    )
    val alarm: AlarmEntity,
) {
    fun toDomain(): Notification =
        Notification(
            bottariId = bottari.id,
            bottariTitle = bottari.title,
            alarm = alarm.toDomain(),
        )
}
