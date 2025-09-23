package com.bottari.data.model.local.bottari

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.Companion.CASCADE
import androidx.room.Index
import androidx.room.PrimaryKey
import com.bottari.domain.model.alarm.Alarm
import com.bottari.domain.model.alarm.AlarmType
import java.time.LocalDate
import java.time.LocalTime

@Entity(
    tableName = "Alarms",
    foreignKeys = [
        ForeignKey(
            entity = BottariEntity::class,
            parentColumns = ["id"],
            childColumns = ["bottariId"],
            onDelete = CASCADE,
        ),
    ],
    indices = [Index("bottariId", unique = true)],
)
data class AlarmEntity(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: Long = 0,
    @ColumnInfo(name = "bottariId")
    val bottariId: Long,
    @ColumnInfo(name = "isActive")
    val isActive: Boolean,
    @ColumnInfo(name = "time")
    val time: LocalTime,
    @ColumnInfo(name = "date")
    val date: LocalDate?,
    @ColumnInfo(name = "repeatDays")
    val repeatDays: List<Int>,
) {
    fun toDomain(): Alarm =
        Alarm(
            id = id,
            isActive = isActive,
            time = time,
            alarmType = toAlarmType(),
            location = null,
        )

    private fun toAlarmType(): AlarmType {
        if (date == null) return AlarmType.Repeat(repeatDays)
        return AlarmType.NonRepeat(date)
    }

    companion object {
        fun fromDomain(
            bottariId: Long,
            alarm: Alarm,
        ): AlarmEntity =
            AlarmEntity(
                id = bottariId,
                bottariId = bottariId,
                isActive = alarm.isActive,
                time = alarm.time,
                date = alarm.alarmType.getAlarmDate(),
                repeatDays = alarm.alarmType.getAlarmRepeatDays(),
            )
    }
}
