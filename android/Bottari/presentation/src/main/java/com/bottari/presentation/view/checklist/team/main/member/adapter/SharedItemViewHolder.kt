package com.bottari.presentation.view.checklist.team.main.member.adapter

import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bottari.presentation.R
import com.bottari.presentation.databinding.ItemChecklistMiniBinding
import com.bottari.presentation.model.ChecklistItemUiModel

class SharedItemViewHolder(
    private val binding: ItemChecklistMiniBinding,
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(item: ChecklistItemUiModel) {
        binding.tvChecklistItemMiniTitle.text = item.name
        val bgColorRes = if (item.isChecked) R.color.primary else R.color.gray_a6a6a6
        val bgColor = ContextCompat.getColor(itemView.context, bgColorRes)
        binding.clChecklistItem.backgroundTintList = ColorStateList.valueOf(bgColor)
    }

    companion object {
        fun from(parent: ViewGroup): SharedItemViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = ItemChecklistMiniBinding.inflate(inflater, parent, false)
            return SharedItemViewHolder(binding)
        }
    }
}
