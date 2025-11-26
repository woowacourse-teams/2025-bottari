package com.bottari.presentation.model.alarm

import android.os.Parcelable
import androidx.compose.runtime.Immutable
import com.bottari.core.domain.model.alarm.Alarm
import com.bottari.core.domain.model.alarm.AlarmType
import kotlinx.parcelize.Parcelize
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalTime

@Immutable
@Parcelize
data class AlarmUiModel(
    val id: Long? = null,
    val isActive: Boolean,
    val type: AlarmTypeUiModel,
    val time: LocalTime,
    val date: LocalDate,
    val repeatDays: List<RepeatDayUiModel>,
    val locationAlarm: LocationAlarmUiModel? = null,
) : Parcelable {
    val isRepeatEveryDay: Boolean
        get() = repeatDays.count { repeatDay -> repeatDay.isChecked } == DAYS_IN_WEEK

    fun toDomain(): Alarm =
        Alarm(
            id = id,
            isActive = isActive,
            time = time,
            alarmType = toDomainType(),
            location = locationAlarm?.toDomain(),
        )

    private fun toDomainType(): AlarmType =
        when (type) {
            AlarmTypeUiModel.NON_REPEAT -> AlarmType.NonRepeat(date)
            AlarmTypeUiModel.REPEAT -> AlarmType.Repeat(repeatDays.toDomain())
        }

    private fun List<RepeatDayUiModel>.toDomain(): List<Int> =
        this
            .filter { dayOfWeekUiModel -> dayOfWeekUiModel.isChecked }
            .map { dayOfWeekUiModel -> dayOfWeekUiModel.dayOfWeek.value }

    companion object {
        private const val DAYS_IN_WEEK = 7

        val DEFAULT_ALARM_UI_MODEL: AlarmUiModel
            get() =
                AlarmUiModel(
                    type = AlarmTypeUiModel.NON_REPEAT,
                    isActive = false,
                    time = LocalTime.now().plusMinutes(1),
                    date = LocalDate.now(),
                    repeatDays = RepeatDayUiModel.DEFAULT_WEEK,
                )

        fun fromDomain(alarm: Alarm): AlarmUiModel =
            AlarmUiModel(
                id = alarm.id,
                isActive = alarm.isActive,
                time = alarm.time,
                type = alarm.alarmType.toUiModel(),
                date = alarm.alarmType.getDate(),
                repeatDays = alarm.alarmType.getDaysOfWeek().toUiModel(),
                locationAlarm = alarm.location?.let(LocationAlarmUiModel::fromDomain),
            )

        private fun AlarmType.toUiModel(): AlarmTypeUiModel =
            when (this) {
                is AlarmType.NonRepeat -> AlarmTypeUiModel.NON_REPEAT
                is AlarmType.Repeat -> AlarmTypeUiModel.REPEAT
            }

        private fun AlarmType.getDate(): LocalDate {
            if (this is AlarmType.NonRepeat) return date
            return LocalDate.now()
        }

        private fun List<Int>.toUiModel(): List<RepeatDayUiModel> {
            val checkedDays = this.toSet()
            return DayOfWeek.entries.map { dayOfWeek ->
                RepeatDayUiModel(
                    dayOfWeek = dayOfWeek,
                    isChecked = dayOfWeek.value in checkedDays,
                )
            }
        }
    }
}
