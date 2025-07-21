package com.bottari.presentation.view.edit.personal.main.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bottari.presentation.databinding.ItemChecklistMiniBinding
import com.bottari.presentation.model.ItemUiModel

class PersonalBottariEditItemViewHolder private constructor(
    private val binding: ItemChecklistMiniBinding,
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(item: ItemUiModel) {
        binding.tvChecklistItemMiniTitle.text = item.name
    }

    companion object {
        fun from(parent: ViewGroup): PersonalBottariEditItemViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = ItemChecklistMiniBinding.inflate(inflater, parent, false)
            return PersonalBottariEditItemViewHolder(binding)
        }
    }
}
