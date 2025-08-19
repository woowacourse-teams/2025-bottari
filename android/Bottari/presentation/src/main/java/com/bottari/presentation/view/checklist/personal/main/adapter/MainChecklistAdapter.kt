package com.bottari.presentation.view.checklist.personal.main.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.bottari.presentation.model.ChecklistItemUiModel
import com.bottari.presentation.view.checklist.personal.main.listener.OnChecklistItemClickListener

class MainChecklistAdapter(
    private val onChecklistItemClickListener: OnChecklistItemClickListener,
) : ListAdapter<ChecklistItemUiModel, MainChecklistViewHolder>(DiffUtil) {
    override fun onBindViewHolder(
        holder: MainChecklistViewHolder,
        position: Int,
    ) = holder.bind(getItem(position))

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): MainChecklistViewHolder = MainChecklistViewHolder.from(parent, onChecklistItemClickListener)

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
