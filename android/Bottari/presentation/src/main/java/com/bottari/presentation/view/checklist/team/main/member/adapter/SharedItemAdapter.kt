package com.bottari.presentation.view.checklist.team.main.member.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.bottari.presentation.model.bottari.PersonalChecklistItemUiModel

class SharedItemAdapter : ListAdapter<PersonalChecklistItemUiModel, SharedItemViewHolder>(DiffUtil) {
    override fun onBindViewHolder(
        holder: SharedItemViewHolder,
        position: Int,
    ) {
        holder.bind(getItem(position))
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): SharedItemViewHolder = SharedItemViewHolder.from(parent)

    companion object {
        private val DiffUtil =
            object : DiffUtil.ItemCallback<PersonalChecklistItemUiModel>() {
                override fun areContentsTheSame(
                    oldItem: PersonalChecklistItemUiModel,
                    newItem: PersonalChecklistItemUiModel,
                ): Boolean = oldItem == newItem

                override fun areItemsTheSame(
                    oldItem: PersonalChecklistItemUiModel,
                    newItem: PersonalChecklistItemUiModel,
                ): Boolean = oldItem.id == newItem.id
            }
    }
}
