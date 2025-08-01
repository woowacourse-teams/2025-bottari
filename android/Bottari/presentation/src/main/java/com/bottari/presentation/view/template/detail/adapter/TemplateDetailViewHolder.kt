package com.bottari.presentation.view.template.detail.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bottari.presentation.databinding.ItemTemplateDetailItemBinding
import com.bottari.presentation.model.BottariTemplateItemUiModel

class TemplateDetailViewHolder private constructor(
    private val binding: ItemTemplateDetailItemBinding,
) : RecyclerView.ViewHolder(binding.root) {
    private var bottariTemplateId: Long? = null

    fun bind(bottariItem: BottariTemplateItemUiModel) {
        bottariTemplateId = bottariItem.id
        binding.tvPersonalItemName.text = bottariItem.name
    }

    companion object {
        fun from(parent: ViewGroup): TemplateDetailViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = ItemTemplateDetailItemBinding.inflate(inflater, parent, false)
            return TemplateDetailViewHolder(binding)
        }
    }
}
