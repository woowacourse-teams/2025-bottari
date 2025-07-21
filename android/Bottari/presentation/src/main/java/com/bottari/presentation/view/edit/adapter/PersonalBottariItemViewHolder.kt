package com.bottari.presentation.view.edit.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bottari.presentation.databinding.ItemChecklistMiniBinding
import com.bottari.presentation.model.ItemUiModel

class PersonalBottariItemViewHolder private constructor(
    private val binding: ItemChecklistMiniBinding,
) : RecyclerView.ViewHolder(binding.root) {
    private var itemId: Long? = null

    fun bind(item: ItemUiModel) {
        itemId = item.id
        binding.tvChecklistItemMiniTitle.text = item.name
    }

    companion object {
        fun from(
            parent: ViewGroup,
        ): PersonalBottariItemViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = ItemChecklistMiniBinding.inflate(inflater, parent, false)
            return PersonalBottariItemViewHolder(binding)
        }
    }
}
