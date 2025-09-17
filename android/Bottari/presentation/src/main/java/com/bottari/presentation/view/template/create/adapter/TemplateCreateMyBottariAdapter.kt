package com.bottari.presentation.view.template.create.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.bottari.presentation.common.listener.OnItemClickListener
import com.bottari.presentation.model.template.SelectableBottariUiModel

class TemplateCreateMyBottariAdapter(
    private val onItemClickListener: OnItemClickListener,
) : ListAdapter<SelectableBottariUiModel, TemplateCreateMyBottariViewHolder>(DiffUtil) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): TemplateCreateMyBottariViewHolder = TemplateCreateMyBottariViewHolder.from(parent, onItemClickListener)

    override fun onBindViewHolder(
        holder: TemplateCreateMyBottariViewHolder,
        position: Int,
    ) = holder.bind(getItem(position))

    companion object {
        private val DiffUtil =
            object : DiffUtil.ItemCallback<SelectableBottariUiModel>() {
                override fun areContentsTheSame(
                    oldItem: SelectableBottariUiModel,
                    newItem: SelectableBottariUiModel,
                ): Boolean = oldItem == newItem

                override fun areItemsTheSame(
                    oldItem: SelectableBottariUiModel,
                    newItem: SelectableBottariUiModel,
                ): Boolean = oldItem.id == newItem.id
            }
    }
}
