package com.bottari.presentation.view.team.status.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.bottari.presentation.model.BottariItemUiModel

class SharedItemAdapter : ListAdapter<BottariItemUiModel, SharedItemViewHolder>(DiffUtil) {
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
