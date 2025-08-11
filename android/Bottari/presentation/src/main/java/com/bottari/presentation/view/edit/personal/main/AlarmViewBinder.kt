package com.bottari.presentation.view.edit.personal.main

import android.content.Context
import androidx.core.view.isVisible
import com.bottari.presentation.R
import com.bottari.presentation.common.extension.formatWithPattern
import com.bottari.presentation.databinding.FragmentPersonalBottariEditBinding
import com.bottari.presentation.model.AlarmTypeUiModel
import com.bottari.presentation.model.AlarmUiModel
import java.time.format.TextStyle
import java.util.Locale

class AlarmViewBinder(
    private val context: Context,
) {
    private val dateFormat: String by lazy {
        context.getString(R.string.common_format_date_alarm)
    }
    private val timeFormat: String by lazy {
        context.getString(R.string.common_format_time_alarm)
    }
    private val separator: String by lazy {
        context.getString(R.string.common_separator_text)
    }

    fun bind(
        binding: FragmentPersonalBottariEditBinding,
        uiState: PersonalBottariEditUiState,
    ) {
        val isSwitchChecked = uiState.isAlarmActive
        val alarm = uiState.alarm
        val hasAlarm = alarm != null
        binding.switchAlarm.isChecked = alarm?.isActive ?: false
        toggleAlarmView(binding, isSwitchChecked, hasAlarm)

        if (alarm == null) return

        with(binding.viewAlarmItem) {
            tvChecklistAlarmType.text =
                when (alarm.type) {
                    AlarmTypeUiModel.NON_REPEAT -> alarm.date.formatWithPattern(dateFormat)
                    AlarmTypeUiModel.REPEAT -> {
                        if (alarm.isRepeatEveryDay) {
                            context.getString(R.string.bottari_item_alarm_repeat_everyday_text)
                        } else {
                            formatEveryWeek(alarm)
                        }
                    }
                }
            tvChecklistAlarmTime.text = alarm.time.formatWithPattern(timeFormat)
        }
    }

    private fun toggleAlarmView(
        binding: FragmentPersonalBottariEditBinding,
        isActive: Boolean,
        hasAlarm: Boolean,
    ) {
        val showEmpty = isActive && !hasAlarm
        val showFilled = isActive && hasAlarm

        binding.tvClickEditAlarmTitle.isVisible = showEmpty
        binding.tvClickEditAlarmDescription.isVisible = showEmpty
        binding.viewClickEditAlarm.isVisible = showEmpty
        binding.tvClickEditAlarmDescriptionNotEmpty.isVisible = showFilled
        binding.viewAlarmItem.clAlarmItem.isVisible = showFilled
    }

    private fun formatEveryWeek(alarm: AlarmUiModel): String {
        val checkedDays = alarm.repeatDays.filter { dayOfWeek -> dayOfWeek.isChecked }
        return buildString {
            append(context.getString(R.string.bottari_item_alarm_repeat_everyweek_text))
            append(separator)
            append(
                checkedDays.joinToString {
                    it.dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.getDefault())
                },
            )
        }
    }
}
