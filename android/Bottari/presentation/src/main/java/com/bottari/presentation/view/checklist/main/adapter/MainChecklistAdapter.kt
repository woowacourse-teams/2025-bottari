package com.bottari.presentation.view.checklist.main.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.bottari.presentation.model.BottariItemUiModel
import com.bottari.presentation.view.checklist.main.listener.OnChecklistItemClickListener

class MainChecklistAdapter(
    private val onChecklistItemClickListener: OnChecklistItemClickListener,
) : ListAdapter<BottariItemUiModel, MainChecklistViewHolder>(DiffUtil) {
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
            object : DiffUtil.ItemCallback<BottariItemUiModel>() {
                override fun areContentsTheSame(
                    oldItem: BottariItemUiModel,
                    newItem: BottariItemUiModel,
                ): Boolean = oldItem == newItem

                override fun areItemsTheSame(
                    oldItem: BottariItemUiModel,
                    newItem: BottariItemUiModel,
                ): Boolean = oldItem.id == newItem.id
            }
    }
}
