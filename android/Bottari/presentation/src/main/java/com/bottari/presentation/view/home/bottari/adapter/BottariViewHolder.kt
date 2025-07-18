package com.bottari.presentation.view.home.bottari.adapter

import android.graphics.drawable.ClipDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.LayerDrawable
import android.graphics.drawable.ShapeDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bottari.presentation.R
import com.bottari.presentation.databinding.ItemBottariBinding
import com.bottari.presentation.extension.formatWithPattern
import com.bottari.presentation.model.AlarmTypeUiModel
import com.bottari.presentation.model.BottariUiModel
import com.bottari.presentation.view.home.bottari.listener.OnBottariClickListener
import java.time.DayOfWeek
import java.time.format.TextStyle
import java.util.Locale

class BottariViewHolder private constructor(
    private val binding: ItemBottariBinding,
    onBottariClickListener: OnBottariClickListener,
) : RecyclerView.ViewHolder(binding.root) {
    private val dateFormat: String = getString(R.string.bottari_item_alarm_date_format)
    private val timeFormat: String = getString(R.string.bottari_item_alarm_time_format)
    private val separator: String = getString(R.string.bottari_item_alarm_info_separator)
    private var bottariId: Long? = null

    init {
        itemView.setOnClickListener {
            bottariId?.let { onBottariClickListener.onClick(it) }
        }
    }

    fun bind(bottari: BottariUiModel) {
        bottariId = bottari.id
        with(binding) {
            clBottariItem.clipToOutline = true
            tvBottariTitle.text = bottari.title
            tvBottariQuantityStatus.text =
                formatQuantityStatus(bottari.checkedQuantity, bottari.totalQuantity)
            tvBottariAlarmInfo.text = formatAlarmInfo(bottari.alarmTypeUiModel)
            updateProgressBar(bottari.checkedQuantity, bottari.totalQuantity)
        }
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

    private fun updateProgressBar(
        checked: Int,
        total: Int,
    ) {
        with(binding.pbBottariItem) {
            max = total
            progress = checked

            val color = getProgressColor(checked, total)
            val updatedDrawable =
                progressDrawable?.mutate()?.let { drawable ->
                    updateProgressColor(drawable, color)
                } ?: return

            progressDrawable = updatedDrawable
        }
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

    private fun getProgressColor(
        checked: Int,
        total: Int,
    ): Int {
        val colorRes = if (checked == total) R.color.primary else R.color.red
        return ContextCompat.getColor(itemView.context, colorRes)
    }

    private fun updateProgressColor(
        drawable: Drawable,
        color: Int,
    ): Drawable {
        if (drawable !is LayerDrawable) return drawable

        val progressLayer = drawable.findDrawableByLayerId(android.R.id.progress)
        val innerDrawable = (progressLayer as? ClipDrawable)?.drawable ?: return drawable

        when (innerDrawable) {
            is ShapeDrawable -> innerDrawable.paint.color = color
            is GradientDrawable -> innerDrawable.setColor(color)
        }

        return drawable
    }

    companion object {
        fun from(
            parent: ViewGroup,
            onBottariClickListener: OnBottariClickListener,
        ): BottariViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = ItemBottariBinding.inflate(inflater, parent, false)
            return BottariViewHolder(binding, onBottariClickListener)
        }
    }
}
