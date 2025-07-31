package com.bottari.presentation.view.home.market.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.bottari.presentation.model.BottariTemplateUiModel
import com.bottari.presentation.view.home.market.listener.OnTemplateClickListener

class TemplateAdapter(
    private val clickListener: OnTemplateClickListener,
) : ListAdapter<BottariTemplateUiModel, TemplateViewHolder>(DiffUtil) {
    override fun onBindViewHolder(
        holder: TemplateViewHolder,
        position: Int,
    ) {
        holder.bind(getItem(position))
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): TemplateViewHolder = TemplateViewHolder.from(parent, clickListener)

    companion object {
        private val DiffUtil =
            object : DiffUtil.ItemCallback<BottariTemplateUiModel>() {
                override fun areContentsTheSame(
                    oldItem: BottariTemplateUiModel,
                    newItem: BottariTemplateUiModel,
                ): Boolean = oldItem == newItem

                override fun areItemsTheSame(
                    oldItem: BottariTemplateUiModel,
                    newItem: BottariTemplateUiModel,
                ): Boolean = oldItem.id == newItem.id
            }
    }
}
