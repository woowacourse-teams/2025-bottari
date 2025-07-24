package com.bottari.presentation.mapper

import com.bottari.domain.model.alarm.Alarm
import com.bottari.domain.model.alarm.AlarmType
import com.bottari.domain.model.alarm.LocationAlarm
import com.bottari.presentation.model.AlarmTypeUiModel
import com.bottari.presentation.model.AlarmUiModel
import com.bottari.presentation.model.DayOfWeekUiModel
import com.bottari.presentation.model.LocationAlarmUiModel
import java.time.DayOfWeek
import java.time.LocalDate

object AlarmMapper {
    fun Alarm.toUiModel(): AlarmUiModel =
        AlarmUiModel(
            id = id,
            isActive = isActive,
            time = time,
            type = alarmType.toUiModel(),
            date = alarmType.getDate(),
            daysOfWeek = alarmType.getDaysOfWeek().toUiModel(),
            locationAlarm = location?.toUiModel(),
        )

    fun AlarmUiModel.toDomain(): Alarm =
        Alarm(
            id = id,
            isActive = isActive,
            time = time,
            alarmType = toDomainType(),
            location = locationAlarm?.toDomain(),
        )

    private fun AlarmType.toUiModel(): AlarmTypeUiModel =
        when (this) {
            is AlarmType.NonRepeat -> AlarmTypeUiModel.NON_REPEAT
            is AlarmType.EveryDayRepeat -> AlarmTypeUiModel.EVERYDAY_REPEAT
            is AlarmType.EveryWeekRepeat -> AlarmTypeUiModel.EVERYWEEK_REPEAT
        }

    private fun AlarmType.getDate(): LocalDate {
        if (this is AlarmType.NonRepeat) return date
        return LocalDate.now()
    }

    private fun AlarmType.getDaysOfWeek(): List<Int> {
        if (this is AlarmType.EveryWeekRepeat) return daysOfWeek
        return emptyList()
    }

    private fun List<Int>.toUiModel(): List<DayOfWeekUiModel> {
        val checkedDays = this.toSet()
        return DayOfWeek.entries.map { dayOfWeek ->
            DayOfWeekUiModel(
                dayOfWeek = dayOfWeek,
                isChecked = dayOfWeek.value in checkedDays,
            )
        }
    }

    private fun AlarmUiModel.toDomainType(): AlarmType =
        when (type) {
            AlarmTypeUiModel.NON_REPEAT -> AlarmType.NonRepeat(date)
            AlarmTypeUiModel.EVERYDAY_REPEAT -> AlarmType.EveryDayRepeat
            AlarmTypeUiModel.EVERYWEEK_REPEAT -> AlarmType.EveryWeekRepeat(daysOfWeek.toDomain())
        }

    private fun List<DayOfWeekUiModel>.toDomain(): List<Int> =
        this
            .filter { dayOfWeekUiModel -> dayOfWeekUiModel.isChecked }
            .map { dayOfWeekUiModel -> dayOfWeekUiModel.dayOfWeek.value }

    private fun LocationAlarmUiModel.toDomain(): LocationAlarm =
        LocationAlarm(
            latitude = latitude,
            longitude = longitude,
            radius = radius,
            isActive = isActive,
        )

    private fun LocationAlarm.toUiModel(): LocationAlarmUiModel =
        LocationAlarmUiModel(
            latitude = latitude,
            longitude = longitude,
            radius = radius,
            isActive = isActive,
        )
}
