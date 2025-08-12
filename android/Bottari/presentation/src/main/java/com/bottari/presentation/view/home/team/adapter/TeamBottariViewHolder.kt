package com.bottari.presentation.view.home.team.adapter

import android.graphics.Color
import android.graphics.drawable.ClipDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.LayerDrawable
import android.graphics.drawable.ShapeDrawable
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.text.style.RelativeSizeSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toDrawable
import androidx.recyclerview.widget.RecyclerView
import com.bottari.presentation.R
import com.bottari.presentation.common.extension.formatWithPattern
import com.bottari.presentation.databinding.ItemBottariBinding
import com.bottari.presentation.databinding.PopupBottariOptionsBinding
import com.bottari.presentation.model.AlarmTypeUiModel
import com.bottari.presentation.model.AlarmUiModel
import com.bottari.presentation.model.RepeatDayUiModel
import com.bottari.presentation.model.TeamBottariUiModel
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.TextStyle
import java.util.Locale

class TeamBottariViewHolder private constructor(
    private val binding: ItemBottariBinding,
    private val bottariEventListener: BottariEventListener,
) : RecyclerView.ViewHolder(binding.root) {
    private val dateFormat = getString(R.string.common_format_date_alarm)
    private val timeFormat = getString(R.string.common_format_time_alarm)
    private val separator = getString(R.string.common_separator_text)
    private var bottariId: Long? = null

    init {
        setupClickListeners()
    }

    fun bind(bottari: TeamBottariUiModel) {
        bottariId = bottari.id
        with(binding) {
            clBottariItem.clipToOutline = true
            tvBottariTitle.text = formatTitleWithMemberCount(bottari.title, bottari.memberCount)
            tvBottariQuantityStatus.text =
                formatQuantityStatus(bottari.checkedQuantity, bottari.totalQuantity)
            tvBottariAlarmInfo.text = formatAlarmInfo(bottari.alarm)
            updateProgressBar(bottari.checkedQuantity, bottari.totalQuantity)
        }
    }

    private fun setupClickListeners() {
        itemView.setOnClickListener {
            bottariId?.let { id ->
                bottariEventListener.onBottariClick(id, binding.tvBottariTitle.text.toString())
            }
        }
        binding.btnBottariMore.setOnClickListener(::showBottariOptionsPopup)
    }

    private fun formatTitleWithMemberCount(
        title: String,
        count: Int,
    ): SpannableStringBuilder =
        SpannableStringBuilder(title)
            .append(" ")
            .append(
                count.toString(),
                ForegroundColorSpan(ContextCompat.getColor(itemView.context, R.color.gray_999999)),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE,
            ).apply {
                setSpan(
                    RelativeSizeSpan(0.9f),
                    length - count.toString().length,
                    length,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE,
                )
            }

    private fun formatQuantityStatus(
        checked: Int,
        total: Int,
    ): String {
        val formatPattern = getString(R.string.bottari_item_status_format)
        return formatPattern.format(checked, total)
    }

    private fun formatAlarmInfo(alarm: AlarmUiModel?): String {
        if (alarm == null) return ""
        return when (alarm.type) {
            AlarmTypeUiModel.NON_REPEAT -> formatNonRepeat(alarm.date, alarm.time)
            AlarmTypeUiModel.REPEAT -> {
                if (alarm.isRepeatEveryDay) {
                    formatEveryDayRepeat(alarm.time)
                } else {
                    formatEveryWeekRepeat(alarm.time, alarm.repeatDays)
                }
            }
        }
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

    private fun formatNonRepeat(
        date: LocalDate,
        time: LocalTime,
    ): String =
        buildString {
            append(date.formatWithPattern(dateFormat))
            append(separator)
            append(time.formatWithPattern(timeFormat))
        }

    private fun formatEveryDayRepeat(time: LocalTime): String =
        buildString {
            append(time.formatWithPattern(timeFormat))
            append(separator)
            append(getString(R.string.bottari_item_alarm_repeat_everyday_text))
        }

    private fun formatEveryWeekRepeat(
        time: LocalTime,
        repeatDays: List<RepeatDayUiModel>,
    ): String {
        val checkedDays = repeatDays.filter { it.isChecked }
        return buildString {
            append(time.formatWithPattern(timeFormat))
            append(separator)
            append(getString(R.string.bottari_item_alarm_repeat_everyweek_text))
            append(separator)
            append(
                checkedDays.joinToString { dayOfWeek ->
                    dayOfWeek.dayOfWeek
                        .getDisplayName(TextStyle.SHORT, Locale.getDefault())
                },
            )
        }
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

    private fun showBottariOptionsPopup(anchorView: View) {
        val binding = PopupBottariOptionsBinding.inflate(LayoutInflater.from(anchorView.context))

        val popupWindow =
            PopupWindow(
                binding.root,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                true,
            ).apply {
                isOutsideTouchable = true
                setBackgroundDrawable(Color.TRANSPARENT.toDrawable())
                elevation = POPUP_ELEVATION
                showAsDropDown(anchorView, POPUP_OFFSET_X, POPUP_OFFSET_Y)
            }

        setPopupClickListeners(binding, popupWindow)
    }

    private fun setPopupClickListeners(
        binding: PopupBottariOptionsBinding,
        popupWindow: PopupWindow,
    ) {
        binding.btnEdit.setOnClickListener {
            bottariId?.let { bottariEventListener.onBottariEditClick(it) }
            popupWindow.dismiss()
        }

        binding.btnDelete.setOnClickListener {
            bottariId?.let { bottariEventListener.onBottariDeleteClick(it) }
            popupWindow.dismiss()
        }
    }

    interface BottariEventListener {
        fun onBottariClick(
            bottariId: Long,
            bottariTitle: String,
        )

        fun onBottariEditClick(bottariId: Long)

        fun onBottariDeleteClick(bottariId: Long)
    }

    companion object {
        private const val POPUP_OFFSET_X = -200
        private const val POPUP_OFFSET_Y = 0
        private const val POPUP_ELEVATION = 10f

        fun from(
            parent: ViewGroup,
            bottariEventListener: BottariEventListener,
        ): TeamBottariViewHolder {
            val binding =
                ItemBottariBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return TeamBottariViewHolder(binding, bottariEventListener)
        }
    }
}
