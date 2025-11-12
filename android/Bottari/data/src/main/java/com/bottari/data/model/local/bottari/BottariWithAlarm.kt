package com.bottari.data.model.local.bottari

import androidx.room.Embedded
import androidx.room.Relation

data class BottariWithAlarm(
    @Embedded
    val bottari: BottariEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "bottariId",
    )
    val alarm: AlarmEntity?,
)
