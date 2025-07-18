package com.bottari.presentation.view.checklist.main.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.bottari.presentation.model.ItemUiModel
import com.bottari.presentation.view.checklist.main.listener.OnChecklistItemClickListener

class MainChecklistAdapter(
    private val onChecklistItemClickListener: OnChecklistItemClickListener,
) : ListAdapter<ItemUiModel, MainChecklistViewHolder>(DiffUtil) {
    override fun onBindViewHolder(
        holder: MainChecklistViewHolder,
        position: Int,
    ) {
        holder.bind(getItem(position))
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): MainChecklistViewHolder = MainChecklistViewHolder.from(parent, onChecklistItemClickListener)

    companion object {
        private val DiffUtil =
            object : DiffUtil.ItemCallback<ItemUiModel>() {
                override fun areContentsTheSame(
                    oldItem: ItemUiModel,
                    newItem: ItemUiModel,
                ): Boolean = oldItem == newItem

                override fun areItemsTheSame(
                    oldItem: ItemUiModel,
                    newItem: ItemUiModel,
                ): Boolean = oldItem.id == newItem.id
            }
    }
}
