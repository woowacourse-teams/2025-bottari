package com.bottari.presentation.view.edit.personal.main.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.bottari.presentation.model.bottari.PersonalChecklistItemUiModel

class PersonalBottariEditItemAdapter :
    ListAdapter<PersonalChecklistItemUiModel, PersonalBottariEditItemViewHolder>(
        DiffUtil,
    ) {
    override fun onBindViewHolder(
        holder: PersonalBottariEditItemViewHolder,
        position: Int,
    ) {
        holder.bind(getItem(position))
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): PersonalBottariEditItemViewHolder = PersonalBottariEditItemViewHolder.from(parent)

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
