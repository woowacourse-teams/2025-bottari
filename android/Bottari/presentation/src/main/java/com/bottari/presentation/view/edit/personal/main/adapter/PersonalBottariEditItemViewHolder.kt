package com.bottari.presentation.view.edit.personal.main.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bottari.presentation.databinding.ItemChipBinding
import com.bottari.presentation.model.bottari.ChecklistItemUiModel

class PersonalBottariEditItemViewHolder private constructor(
    private val binding: ItemChipBinding,
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(item: ChecklistItemUiModel) {
        binding.tvChecklistItemMiniTitle.text = item.name
    }

    companion object {
        fun from(parent: ViewGroup): PersonalBottariEditItemViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = ItemChipBinding.inflate(inflater, parent, false)
            return PersonalBottariEditItemViewHolder(binding)
        }
    }
}
