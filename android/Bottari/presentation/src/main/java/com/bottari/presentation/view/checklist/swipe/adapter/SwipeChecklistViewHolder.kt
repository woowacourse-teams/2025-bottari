package com.bottari.presentation.view.checklist.swipe.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bottari.presentation.databinding.ItemSwipeChecklistBinding
import com.bottari.presentation.model.ItemUiModel

class SwipeChecklistViewHolder private constructor(
    private val binding: ItemSwipeChecklistBinding,
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(item: ItemUiModel) {
        binding.tvSwipeItemTitle.text = item.name
    }

    companion object {
        fun from(parent: ViewGroup): SwipeChecklistViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = ItemSwipeChecklistBinding.inflate(inflater, parent, false)
            return SwipeChecklistViewHolder(binding)
        }
    }
}
