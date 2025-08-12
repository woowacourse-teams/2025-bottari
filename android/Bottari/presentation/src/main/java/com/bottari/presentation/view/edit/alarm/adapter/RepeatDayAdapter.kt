package com.bottari.presentation.view.edit.alarm.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.bottari.presentation.model.RepeatDayUiModel
import com.bottari.presentation.view.edit.alarm.listener.OnRepeatDayClickListener

class RepeatDayAdapter(
    private val onRepeatDayClickListener: OnRepeatDayClickListener,
) : ListAdapter<RepeatDayUiModel, RepeatDayViewHolder>(DiffUtil) {
    override fun onBindViewHolder(
        holder: RepeatDayViewHolder,
        position: Int,
    ) {
        holder.bind(getItem(position))
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): RepeatDayViewHolder = RepeatDayViewHolder.from(parent, onRepeatDayClickListener)

    companion object {
        private val DiffUtil =
            object : DiffUtil.ItemCallback<RepeatDayUiModel>() {
                override fun areContentsTheSame(
                    oldItem: RepeatDayUiModel,
                    newItem: RepeatDayUiModel,
                ): Boolean = oldItem == newItem

                override fun areItemsTheSame(
                    oldItem: RepeatDayUiModel,
                    newItem: RepeatDayUiModel,
                ): Boolean = oldItem.dayOfWeek == newItem.dayOfWeek
            }
    }
}
