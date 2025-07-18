package com.bottari.presentation.view.checklist.main.adapter

import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bottari.presentation.R
import com.bottari.presentation.databinding.ItemChecklistBinding
import com.bottari.presentation.model.ItemUiModel

class MainChecklistViewHolder private constructor(
    private val binding: ItemChecklistBinding,
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(item: ItemUiModel) {
        binding.tvChecklistItemTitle.text =
            itemView.context.getString(R.string.checklist_item_title_prefix, item.name)

        updateCheckedState(item.isChecked)
    }

    private fun updateCheckedState(isChecked: Boolean) {
        val bgColorRes = if (isChecked) R.color.primary else R.color.white
        val bgColor = ContextCompat.getColor(itemView.context, bgColorRes)

        val textColorRes = if (isChecked) R.color.white else R.color.black
        val textColor = ContextCompat.getColor(itemView.context, textColorRes)

        binding.clChecklistItem.backgroundTintList = ColorStateList.valueOf(bgColor)
        binding.tvChecklistItemTitle.setTextColor(textColor)
    }

    companion object {
        fun from(parent: ViewGroup): MainChecklistViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = ItemChecklistBinding.inflate(inflater, parent, false)
            return MainChecklistViewHolder(binding)
        }
    }
}
