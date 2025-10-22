package com.bottari.presentation.view.template.create.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.bottari.presentation.model.bottari.PersonalChecklistItemUiModel

class TemplateCreateMyBottariItemAdapter : ListAdapter<PersonalChecklistItemUiModel, TemplateCreateMyBottariItemViewHolder>(DiffUtil) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): TemplateCreateMyBottariItemViewHolder = TemplateCreateMyBottariItemViewHolder.from(parent)

    override fun onBindViewHolder(
        holder: TemplateCreateMyBottariItemViewHolder,
        position: Int,
    ) = holder.bind(getItem(position))

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
