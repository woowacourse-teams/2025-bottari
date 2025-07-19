package com.bottari.presentation.view.edit.personal.item.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.bottari.presentation.model.ItemUiModel
import com.bottari.presentation.view.edit.personal.item.listener.OnEditItemClickListener

class PersonalItemEditAdapter(
    private val onEditItemClickListener: OnEditItemClickListener,
) : ListAdapter<ItemUiModel, PersonalItemEditViewHolder>(DiffUtil) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PersonalItemEditViewHolder {
        return PersonalItemEditViewHolder.from(parent, onEditItemClickListener)
    }

    override fun onBindViewHolder(holder: PersonalItemEditViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

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



