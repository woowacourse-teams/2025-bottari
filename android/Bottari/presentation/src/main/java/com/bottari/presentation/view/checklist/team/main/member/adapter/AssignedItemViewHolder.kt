package com.bottari.presentation.view.checklist.team.main.member.adapter

import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bottari.presentation.R
import com.bottari.presentation.model.bottari.ChecklistItemUiModel

class AssignedItemViewHolder(
    private val binding: ItemChipBinding,
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(item: ChecklistItemUiModel) {
        binding.tvChecklistItemMiniTitle.text = item.name
        val bgColorRes = if (item.isChecked) R.color.product_type_assigned else R.color.gray_500
        val bgColor = ContextCompat.getColor(itemView.context, bgColorRes)
        binding.root.backgroundTintList = ColorStateList.valueOf(bgColor)
    }

    companion object {
        fun from(parent: ViewGroup): AssignedItemViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = ItemChipBinding.inflate(inflater, parent, false)
            return AssignedItemViewHolder(binding)
        }
    }
}
