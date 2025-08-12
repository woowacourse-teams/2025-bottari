package com.bottari.presentation.view.home.bottari.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.bottari.presentation.model.BottariUiModel

class BottariAdapter(
    private val bottariEventListener: BottariViewHolder.BottariEventListener,
) : ListAdapter<BottariUiModel, BottariViewHolder>(DiffUtil) {
    override fun onBindViewHolder(
        holder: BottariViewHolder,
        position: Int,
    ) {
        holder.bind(getItem(position))
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): BottariViewHolder = BottariViewHolder.from(parent, bottariEventListener)

    companion object {
        private val DiffUtil =
            object : DiffUtil.ItemCallback<BottariUiModel>() {
                override fun areContentsTheSame(
                    oldItem: BottariUiModel,
                    newItem: BottariUiModel,
                ): Boolean = oldItem == newItem

                override fun areItemsTheSame(
                    oldItem: BottariUiModel,
                    newItem: BottariUiModel,
                ): Boolean = oldItem.title == newItem.title
            }
    }
}
