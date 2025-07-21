package com.bottari.presentation.view.edit.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.bottari.presentation.model.ItemUiModel

class PersonalBottariItemAdapter :
    ListAdapter<ItemUiModel, PersonalBottariItemViewHolder>(
        DiffUtil,
    ) {
    override fun onBindViewHolder(
        holder: PersonalBottariItemViewHolder,
        position: Int,
    ) {
        holder.bind(getItem(position))
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): PersonalBottariItemViewHolder = PersonalBottariItemViewHolder.from(parent)

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
