package com.bottari.data.model.notification

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate
import java.time.LocalTime

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
)
