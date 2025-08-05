package com.bottari.presentation.view.edit.alarm.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.bottari.presentation.model.DayOfWeekUiModel
import com.bottari.presentation.view.edit.alarm.listener.OnDayOfWeekClickListener

class DayOfWeekAdapter(
    private val onDayOfWeekClickListener: OnDayOfWeekClickListener,
) : ListAdapter<DayOfWeekUiModel, DayOfWeekViewHolder>(DiffUtil) {
    override fun onBindViewHolder(
        holder: DayOfWeekViewHolder,
        position: Int,
    ) {
        holder.bind(getItem(position))
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): DayOfWeekViewHolder = DayOfWeekViewHolder.from(parent, onDayOfWeekClickListener)

    companion object {
        private val DiffUtil =
            object : DiffUtil.ItemCallback<DayOfWeekUiModel>() {
                override fun areContentsTheSame(
                    oldItem: DayOfWeekUiModel,
                    newItem: DayOfWeekUiModel,
                ): Boolean = oldItem == newItem

                override fun areItemsTheSame(
                    oldItem: DayOfWeekUiModel,
                    newItem: DayOfWeekUiModel,
                ): Boolean = oldItem.dayOfWeek == newItem.dayOfWeek
            }
    }
}
