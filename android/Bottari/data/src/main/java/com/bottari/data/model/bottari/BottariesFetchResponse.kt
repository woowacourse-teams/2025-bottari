package com.bottari.data.model.bottari

import com.bottari.data.common.util.LocalDateSerializer
import com.bottari.data.common.util.LocalTimeSerializer
import com.bottari.domain.model.alarm.Alarm
import com.bottari.domain.model.alarm.AlarmType
import com.bottari.domain.model.alarm.LocationAlarm
import com.bottari.domain.model.bottari.Bottari
import com.bottari.domain.model.bottari.BottariState
import com.bottari.domain.model.bottari.item.BottariItemCount
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.time.LocalDate
import java.time.LocalTime

private const val ERROR_MISSING_DATE = "NON_REPEAT 유형의 알람에는 날짜 정보가 필요합니다."
private const val ERROR_UNKNOWN_ALARM_TYPE = "지원하지 않는 알람 유형입니다: %s"
private const val NON_REPEAT = "NON_REPEAT"
private const val EVERY_DAY_REPEAT = "EVERY_DAY_REPEAT"
private const val EVERY_WEEK_REPEAT = "EVERY_WEEK_REPEAT"

@Serializable
data class BottariesFetchResponse(
    @SerialName("id")
    val id: Long,
    @SerialName("title")
    val title: String,
    @SerialName("alarm")
    val alarm: BottariesAlarmFetchResponse?,
    @SerialName("checkedItemsCount")
    val checkedItemsCount: Int,
    @SerialName("totalItemsCount")
    val totalItemsCount: Int,
) {
    fun toDomain(): BottariState =
        BottariState(
            bottari =
                Bottari(
                    id = id,
                    title = title,
                    alarm = alarm?.toDomain(),
                    items = emptyList(),
                ),
            itemCount =
                BottariItemCount(
                    checkedQuantity = checkedItemsCount,
                    totalQuantity = totalItemsCount,
                ),
        )
}

@Serializable
data class BottariesAlarmFetchResponse(
    @SerialName("id")
    val id: Long,
    @SerialName("isActive")
    val isActive: Boolean,
    @SerialName("location")
    val location: BottariesAlarmLocationFetchResponse?,
    @SerialName("routine")
    val routine: BottariesAlarmRoutineFetchResponse,
) {
    fun toDomain(): Alarm =
        Alarm(
            id = id,
            isActive = isActive,
            time = routine.time,
            alarmType = routine.toDomain(),
            location = location?.toDomain(),
        )
}

@Serializable
data class BottariesAlarmLocationFetchResponse(
    @SerialName("isActive")
    val isActive: Boolean,
    @SerialName("latitude")
    val latitude: Double,
    @SerialName("longitude")
    val longitude: Double,
    @SerialName("radius")
    val radius: Int,
) {
    fun toDomain(): LocationAlarm =
        LocationAlarm(
            latitude = latitude,
            longitude = longitude,
            radius = radius,
            isActive = isActive,
        )
}

@Serializable
data class BottariesAlarmRoutineFetchResponse(
    @SerialName("date")
    @Serializable(with = LocalDateSerializer::class)
    val date: LocalDate?,
    @SerialName("time")
    @Serializable(with = LocalTimeSerializer::class)
    val time: LocalTime,
    @SerialName("dayOfWeeks")
    val dayOfWeeks: List<Int>,
    @SerialName("type")
    val type: String,
) {
    fun toDomain(): AlarmType =
        when (type.uppercase()) {
            NON_REPEAT ->
                AlarmType.NonRepeat(
                    date = date ?: throw IllegalArgumentException(ERROR_MISSING_DATE),
                )

            EVERY_DAY_REPEAT,
            EVERY_WEEK_REPEAT,
            -> AlarmType.Repeat(dayOfWeeks)

            else -> throw IllegalArgumentException(ERROR_UNKNOWN_ALARM_TYPE.format(type))
        }
}
