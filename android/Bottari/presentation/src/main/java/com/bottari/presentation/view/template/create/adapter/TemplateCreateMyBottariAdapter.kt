package com.bottari.presentation.view.template.create.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.bottari.presentation.common.listener.OnItemClickListener
import com.bottari.presentation.model.MyBottariUiModel

class TemplateCreateMyBottariAdapter(
    private val onItemClickListener: OnItemClickListener,
) : ListAdapter<MyBottariUiModel, TemplateCreateMyBottariViewHolder>(DiffUtil) {
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
            object : DiffUtil.ItemCallback<MyBottariUiModel>() {
                override fun areContentsTheSame(
                    oldItem: MyBottariUiModel,
                    newItem: MyBottariUiModel,
                ): Boolean = oldItem == newItem

                override fun areItemsTheSame(
                    oldItem: MyBottariUiModel,
                    newItem: MyBottariUiModel,
                ): Boolean = oldItem.id == newItem.id
            }
    }
}
