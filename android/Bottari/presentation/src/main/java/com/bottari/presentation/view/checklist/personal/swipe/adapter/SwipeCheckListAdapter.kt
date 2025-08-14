package com.bottari.presentation.view.checklist.personal.swipe.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.bottari.presentation.model.BottariItemUiModel

class SwipeCheckListAdapter : ListAdapter<BottariItemUiModel, SwipeChecklistViewHolder>(DiffUtil) {
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
