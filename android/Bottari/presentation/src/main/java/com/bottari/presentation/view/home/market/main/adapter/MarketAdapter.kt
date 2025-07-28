package com.bottari.presentation.view.home.market.main.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.bottari.presentation.model.BottariTemplateUiModel
import com.bottari.presentation.view.home.market.main.listener.OnBottariTemplateClickListener

class MarketAdapter(
    private val clickListener: OnBottariTemplateClickListener,
) : ListAdapter<BottariTemplateUiModel, MarketViewHolder>(DiffUtil) {
    override fun onBindViewHolder(
        holder: MarketViewHolder,
        position: Int,
    ) {
        holder.bind(getItem(position))
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): MarketViewHolder = MarketViewHolder.from(parent, clickListener)

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
