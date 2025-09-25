package com.bottari.presentation.view.template.create.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.bottari.presentation.model.bottari.ChecklistItemUiModel

class TemplateCreateMyBottariItemAdapter : ListAdapter<ChecklistItemUiModel, TemplateCreateMyBottariItemViewHolder>(DiffUtil) {
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
            object : DiffUtil.ItemCallback<ChecklistItemUiModel>() {
                override fun areContentsTheSame(
                    oldItem: ChecklistItemUiModel,
                    newItem: ChecklistItemUiModel,
                ): Boolean = oldItem == newItem

                override fun areItemsTheSame(
                    oldItem: ChecklistItemUiModel,
                    newItem: ChecklistItemUiModel,
                ): Boolean = oldItem.id == newItem.id
            }
    }
}
