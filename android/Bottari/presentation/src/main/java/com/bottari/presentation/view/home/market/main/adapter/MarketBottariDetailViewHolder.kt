package com.bottari.presentation.view.home.market.main.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bottari.presentation.databinding.ItemMarketBottariDetailItemBinding
import com.bottari.presentation.model.BottariTemplateItemUiModel

class MarketBottariDetailViewHolder private constructor(
    private val binding: ItemMarketBottariDetailItemBinding,
) : RecyclerView.ViewHolder(binding.root) {
    private var bottariTemplateId: Long? = null

    fun bind(bottariItem: BottariTemplateItemUiModel) {
        bottariTemplateId = bottariItem.id
        binding.tvPersonalItemName.text = bottariItem.name
    }

    companion object {
        fun from(parent: ViewGroup): MarketBottariDetailViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = ItemMarketBottariDetailItemBinding.inflate(inflater, parent, false)
            return MarketBottariDetailViewHolder(binding)
        }
    }
}
