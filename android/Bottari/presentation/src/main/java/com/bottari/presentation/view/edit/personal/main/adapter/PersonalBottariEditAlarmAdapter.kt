package com.bottari.presentation.view.edit.personal.main.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.bottari.presentation.model.AlarmUiModel

class PersonalBottariEditAlarmAdapter :
    ListAdapter<AlarmUiModel, PersonalBottariEditAlarmViewHolder>(
        DiffUtil,
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
            object : DiffUtil.ItemCallback<AlarmUiModel>() {
                override fun areContentsTheSame(
                    oldItem: AlarmUiModel,
                    newItem: AlarmUiModel,
                ): Boolean = oldItem == newItem

                override fun areItemsTheSame(
                    oldItem: AlarmUiModel,
                    newItem: AlarmUiModel,
                ): Boolean = oldItem.id == newItem.id
            }
    }
}
