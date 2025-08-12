package com.bottari.presentation.view.edit.alarm.adapter

import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bottari.presentation.R
import com.bottari.presentation.databinding.ItemDayOfWeekBinding
import com.bottari.presentation.model.RepeatDayUiModel
import com.bottari.presentation.view.edit.alarm.listener.OnRepeatDayClickListener
import java.time.format.TextStyle
import java.util.Locale

class RepeatDayViewHolder private constructor(
    private val binding: ItemDayOfWeekBinding,
    private val onRepeatDayClickListener: OnRepeatDayClickListener,
) : RecyclerView.ViewHolder(binding.root) {
    private lateinit var repeatDay: RepeatDayUiModel

    init {
        binding.btnDayOfWeek.setOnClickListener {
            onRepeatDayClickListener.onClick(repeatDay)
        }
    }

    fun bind(repeatDay: RepeatDayUiModel) {
        this.repeatDay = repeatDay
        binding.btnDayOfWeek.text =
            repeatDay.dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.getDefault())
        updateDayOfWeekState(repeatDay.isChecked)
    }

    private fun updateDayOfWeekState(isChecked: Boolean) {
        val bgColorRes = if (isChecked) R.color.primary else R.color.gray_f2f2f5
        val bgColor = ContextCompat.getColor(itemView.context, bgColorRes)

        val textColorRes = if (isChecked) R.color.white else R.color.black
        val textColor = ContextCompat.getColor(itemView.context, textColorRes)

        binding.btnDayOfWeek.backgroundTintList = ColorStateList.valueOf(bgColor)
        binding.btnDayOfWeek.setTextColor(textColor)
    }

    companion object {
        fun from(
            parent: ViewGroup,
            onRepeatDayClickListener: OnRepeatDayClickListener,
        ): RepeatDayViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = ItemDayOfWeekBinding.inflate(inflater, parent, false)
            return RepeatDayViewHolder(binding, onRepeatDayClickListener)
        }
    }
}
