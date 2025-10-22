package com.bottari.presentation.view.template.create.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bottari.presentation.databinding.ItemTemplateDetailItemBinding
import com.bottari.presentation.model.bottari.PersonalChecklistItemUiModel

class TemplateCreateMyBottariItemViewHolder private constructor(
    private val binding: ItemTemplateDetailItemBinding,
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(item: PersonalChecklistItemUiModel) {
        binding.tvPersonalItemName.text = item.name
    }

    companion object {
        fun from(parent: ViewGroup): TemplateCreateMyBottariItemViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = ItemTemplateDetailItemBinding.inflate(inflater, parent, false)
            return TemplateCreateMyBottariItemViewHolder(binding)
        }
    }
}
