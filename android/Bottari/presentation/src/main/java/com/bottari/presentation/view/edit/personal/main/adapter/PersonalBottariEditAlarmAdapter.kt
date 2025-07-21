package com.bottari.presentation.view.edit.personal.main.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.bottari.presentation.model.AlarmTypeUiModel

class PersonalBottariEditAlarmAdapter : ListAdapter<AlarmTypeUiModel, PersonalBottariEditAlarmViewHolder>(
    DiffUtil
) {
    override fun onBindViewHolder(
        holder: PersonalBottariEditAlarmViewHolder,
        position: Int,
    ) {
        holder.bind(getItem(position))
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): PersonalBottariEditAlarmViewHolder = PersonalBottariEditAlarmViewHolder.from(parent)

    companion object {
        private val DiffUtil =
            object : DiffUtil.ItemCallback<AlarmTypeUiModel>() {
                override fun areContentsTheSame(
                    oldItem: AlarmTypeUiModel,
                    newItem: AlarmTypeUiModel,
                ): Boolean = oldItem == newItem

                override fun areItemsTheSame(
                    oldItem: AlarmTypeUiModel,
                    newItem: AlarmTypeUiModel,
                ): Boolean = oldItem == newItem
            }
    }
}
