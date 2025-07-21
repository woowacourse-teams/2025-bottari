package com.bottari.presentation.view.edit.personal.main.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bottari.presentation.R
import com.bottari.presentation.databinding.ItemChecklistAlarmBinding
import com.bottari.presentation.extension.formatWithPattern
import com.bottari.presentation.model.AlarmTypeUiModel
import java.time.DayOfWeek
import java.time.format.TextStyle
import java.util.Locale

class PersonalBottariAlarmViewHolder private constructor(
    private val binding: ItemChecklistAlarmBinding,
) : RecyclerView.ViewHolder(binding.root) {
    private val dateFormat: String by lazy {
        itemView.context.getString(R.string.bottari_item_alarm_date_format)
    }
    private val timeFormat: String by lazy {
        itemView.context.getString(R.string.bottari_item_alarm_time_format)
    }
    private val separator: String by lazy {
        itemView.context.getString(R.string.bottari_item_alarm_info_separator)
    }

    fun bind(item: AlarmTypeUiModel) {
        when (item) {
            is AlarmTypeUiModel.EveryDayRepeat -> {
                binding.tvChecklistAlarmType.text = item.formatted()
                binding.tvChecklistAlarmTime.text = item.time.formatWithPattern(timeFormat)
            }

            is AlarmTypeUiModel.EveryWeekRepeat -> {
                binding.tvChecklistAlarmType.text = item.formatted()
                binding.tvChecklistAlarmTime.text = item.time.formatWithPattern(timeFormat)
            }

            is AlarmTypeUiModel.NonRepeat -> {
                binding.tvChecklistAlarmType.text = item.formatted()
                binding.tvChecklistAlarmTime.text = item.time.formatWithPattern(timeFormat)
            }
        }
    }

    private fun AlarmTypeUiModel.NonRepeat.formatted(): String = date.formatWithPattern(dateFormat)

    private fun AlarmTypeUiModel.EveryDayRepeat.formatted(): String =
        itemView.context.getString(R.string.bottari_item_alarm_type_everyday_repeat)

    private fun AlarmTypeUiModel.EveryWeekRepeat.formatted(): String =
        buildString {
            append(itemView.context.getString(R.string.bottari_item_alarm_type_everyweek_repeat))
            append(separator)
            append(
                days.joinToString {
                    DayOfWeek
                        .of(it)
                        .getDisplayName(TextStyle.SHORT, Locale.getDefault())
                },
            )
        }

    companion object {
        fun from(parent: ViewGroup): PersonalBottariAlarmViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = ItemChecklistAlarmBinding.inflate(inflater, parent, false)
            return PersonalBottariAlarmViewHolder(binding)
        }
    }
}
