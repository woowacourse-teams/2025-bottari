package com.bottari.presentation.view.edit.personal.item.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.bottari.presentation.model.bottari.PersonalChecklistItemUiModel
import com.bottari.presentation.view.edit.personal.item.listener.OnEditItemClickListener

class PersonalItemEditAdapter(
    private val onEditItemClickListener: OnEditItemClickListener,
) : ListAdapter<PersonalChecklistItemUiModel, PersonalItemEditViewHolder>(DiffUtil) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): PersonalItemEditViewHolder = PersonalItemEditViewHolder.from(parent, onEditItemClickListener)

    override fun onBindViewHolder(
        holder: PersonalItemEditViewHolder,
        position: Int,
    ) {
        holder.bind(getItem(position))
    }

    companion object {
        private val DiffUtil =
            object : DiffUtil.ItemCallback<PersonalChecklistItemUiModel>() {
                override fun areContentsTheSame(
                    oldItem: PersonalChecklistItemUiModel,
                    newItem: PersonalChecklistItemUiModel,
                ): Boolean = oldItem == newItem

                override fun areItemsTheSame(
                    oldItem: PersonalChecklistItemUiModel,
                    newItem: PersonalChecklistItemUiModel,
                ): Boolean = oldItem.id == newItem.id
            }
    }
}
