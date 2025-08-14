package com.bottari.presentation.view.checklist.team.member.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.bottari.presentation.model.ChecklistItemUiModel

class SharedItemAdapter : ListAdapter<ChecklistItemUiModel, SharedItemViewHolder>(DiffUtil) {
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
            object : DiffUtil.ItemCallback<ChecklistItemUiModel>() {
                override fun areContentsTheSame(
                    oldItem: ChecklistItemUiModel,
                    newItem: ChecklistItemUiModel,
                ): Boolean = oldItem == newItem

                override fun areItemsTheSame(
                    oldItem: ChecklistItemUiModel,
                    newItem: ChecklistItemUiModel,
                ): Boolean = oldItem.id == newItem.id
            }
    }
}
