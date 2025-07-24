package com.bottari.data.mapper

import com.bottari.data.mapper.AlarmMapper.toDomain
import com.bottari.data.model.bottari.AlarmResponse
import com.bottari.data.model.bottari.BottariResponse
import com.bottari.data.model.bottari.FetchBottariesResponse
import com.bottari.data.model.bottari.ItemResponse
import com.bottari.data.model.bottari.LocationResponse
import com.bottari.data.model.bottari.RoutineResponse
import com.bottari.domain.model.alarm.Alarm
import com.bottari.domain.model.alarm.AlarmType
import com.bottari.domain.model.alarm.LocationAlarm
import com.bottari.domain.model.bottari.Bottari
import com.bottari.domain.model.bottari.BottariDetail
import com.bottari.domain.model.bottari.BottariItem

object BottariMapper {
    fun List<FetchBottariesResponse>.toDomain(): List<Bottari> = mapNotNull { it.toDomain() }

    private fun FetchBottariesResponse.toDomain(): Bottari =
        Bottari(
            alarm = alarmResponse?.toDomain(),
            checkedQuantity = checkedItemsCount,
            id = id,
            title = title,
            totalQuantity = totalItemsCount,
        )

    private fun AlarmResponse.toDomain(): Alarm? {
        val alarmType = routine.toDomainType() ?: return null
        return Alarm(
            id = null,
            isActive = isActive,
            time = routine.time,
            alarmType = alarmType,
            location = location?.toDomain(),
        )
    }

    private fun LocationResponse.toDomain(): LocationAlarm =
        LocationAlarm(
            isActive = isActive,
            latitude = latitude,
            longitude = longitude,
            radius = radius,
        )

    private fun RoutineResponse.toDomainType(): AlarmType? =
        when (type) {
            "NON_REPEAT" -> date?.let { AlarmType.NonRepeat(it) }
            "EVERY_DAY_REPEAT" -> AlarmType.EveryDayRepeat
            "EVERY_WEEK_REPEAT" -> AlarmType.EveryWeekRepeat(dayOfWeeks)
            else -> null
        }

    fun BottariResponse.toDomain(): BottariDetail =
        BottariDetail(
            id = id,
            title = title,
            alarm = alarm?.toDomain(),
            items = items.map { it.toDomain() },
        )

    private fun ItemResponse.toDomain(): BottariItem =
        BottariItem(
            id = id,
            name = name,
            isChecked = false,
        )
}
