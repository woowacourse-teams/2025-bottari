package com.bottari.presentation.view.home.bottari.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.recyclerview.widget.RecyclerView
import com.bottari.presentation.R
import com.bottari.presentation.databinding.ItemBottariBinding
import com.bottari.presentation.extension.formatWithPattern
import com.bottari.presentation.model.AlarmTypeUiModel
import com.bottari.presentation.model.BottariUiModel
import java.time.DayOfWeek
import java.time.format.TextStyle
import java.util.Locale

class BottariViewHolder private constructor(
    private val binding: ItemBottariBinding,
) : RecyclerView.ViewHolder(binding.root) {
    private val dateFormat: String = getString(R.string.bottari_item_alarm_date_format)
    private val timeFormat: String = getString(R.string.bottari_item_alarm_time_format)
    private val separator: String = getString(R.string.bottari_item_alarm_info_separator)

    fun bind(bottari: BottariUiModel) =
        with(binding) {
            tvBottariTitle.text = bottari.title
            tvBottariQuantityStatus.text =
                formatQuantityStatus(bottari.checkedQuantity, bottari.totalQuantity)
            tvBottariAlarmInfo.text = formatAlarmInfo(bottari.alarmTypeUiModel)
        }

    private fun formatQuantityStatus(
        checked: Int,
        total: Int,
    ): String {
        val formatPattern = getString(R.string.bottari_item_quantity_status_format)
        return formatPattern.format(checked, total)
    }

    private fun formatAlarmInfo(alarmType: AlarmTypeUiModel): String =
        when (alarmType) {
            is AlarmTypeUiModel.NonRepeat -> alarmType.formatted()

            is AlarmTypeUiModel.EveryDayRepeat -> alarmType.formatted()

            is AlarmTypeUiModel.EveryWeekRepeat -> alarmType.formatted()
        }

    private fun getString(
        @StringRes resId: Int,
    ): String = itemView.context.getString(resId)

    private fun AlarmTypeUiModel.NonRepeat.formatted(): String =
        buildString {
            append(date.formatWithPattern(dateFormat))
            append(separator)
            append(time.formatWithPattern(timeFormat))
        }

    private fun AlarmTypeUiModel.EveryDayRepeat.formatted(): String =
        buildString {
            append(time.formatWithPattern(timeFormat))
            append(separator)
            append(getString(R.string.bottari_item_alarm_type_everyday_repeat))
        }

    private fun AlarmTypeUiModel.EveryWeekRepeat.formatted(): String =
        buildString {
            append(time.formatWithPattern(timeFormat))
            append(separator)
            append(getString(R.string.bottari_item_alarm_type_everyweek_repeat))
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
        fun from(parent: ViewGroup): BottariViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = ItemBottariBinding.inflate(inflater, parent, false)
            return BottariViewHolder(binding)
        }
    }
}
