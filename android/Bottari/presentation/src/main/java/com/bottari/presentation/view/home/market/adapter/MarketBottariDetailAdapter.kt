package com.bottari.presentation.view.home.market.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.bottari.presentation.model.BottariTemplateItemUiModel

class MarketBottariDetailAdapter : ListAdapter<BottariTemplateItemUiModel, MarketBottariDetailViewHolder>(DiffUtil) {
    override fun onBindViewHolder(
        holder: MarketBottariDetailViewHolder,
        position: Int,
    ) {
        holder.bind(getItem(position))
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): MarketBottariDetailViewHolder = MarketBottariDetailViewHolder.from(parent)

    companion object {
        private val DiffUtil =
            object : DiffUtil.ItemCallback<BottariTemplateItemUiModel>() {
                override fun areContentsTheSame(
                    oldItem: BottariTemplateItemUiModel,
                    newItem: BottariTemplateItemUiModel,
                ): Boolean = oldItem == newItem

                override fun areItemsTheSame(
                    oldItem: BottariTemplateItemUiModel,
                    newItem: BottariTemplateItemUiModel,
                ): Boolean = oldItem.id == newItem.id
            }
    }
}
