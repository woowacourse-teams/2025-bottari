package com.bottari.presentation.view.edit.personal.main.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bottari.presentation.R
import com.bottari.presentation.databinding.ItemChecklistAlarmBinding
import com.bottari.presentation.extension.formatWithPattern
import com.bottari.presentation.model.AlarmTypeUiModel
import com.bottari.presentation.model.AlarmUiModel
import java.time.format.TextStyle
import java.util.Locale

class PersonalBottariEditAlarmViewHolder private constructor(
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

    fun bind(item: AlarmUiModel) {
        when (item.type) {
            AlarmTypeUiModel.NON_REPEAT -> {
                binding.tvChecklistAlarmType.text = item.date.formatWithPattern(dateFormat)
                binding.tvChecklistAlarmTime.text = item.time.formatWithPattern(timeFormat)
            }

            AlarmTypeUiModel.EVERYDAY_REPEAT -> {
                binding.tvChecklistAlarmType.text =
                    itemView.context.getString(R.string.bottari_item_alarm_type_everyday_repeat)
                binding.tvChecklistAlarmTime.text = item.time.formatWithPattern(timeFormat)
            }

            AlarmTypeUiModel.EVERYWEEK_REPEAT -> {
                binding.tvChecklistAlarmType.text = item.formatted()
                binding.tvChecklistAlarmTime.text = item.time.formatWithPattern(timeFormat)
            }
        }
    }

    private fun AlarmUiModel.formatted(): String {
        val checkedDays = daysOfWeek.filter { it.isChecked }
        return buildString {
            append(itemView.context.getString(R.string.bottari_item_alarm_type_everyweek_repeat))
            append(separator)
            append(
                checkedDays.joinToString { dayOfWeek ->
                    dayOfWeek.dayOfWeek
                        .getDisplayName(TextStyle.SHORT, Locale.getDefault())
                },
            )
        }
    }

    companion object {
        fun from(parent: ViewGroup): PersonalBottariEditAlarmViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = ItemChecklistAlarmBinding.inflate(inflater, parent, false)
            return PersonalBottariEditAlarmViewHolder(binding)
        }
    }
}
