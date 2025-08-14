package com.bottari.presentation.view.checklist.personal.swipe.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.bottari.presentation.model.ChecklistItemUiModel

class SwipeCheckListAdapter : ListAdapter<ChecklistItemUiModel, SwipeChecklistViewHolder>(DiffUtil) {
    override fun onBindViewHolder(
        holder: SwipeChecklistViewHolder,
        position: Int,
    ) {
        holder.bind(getItem(position))
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): SwipeChecklistViewHolder = SwipeChecklistViewHolder.from(parent)

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
