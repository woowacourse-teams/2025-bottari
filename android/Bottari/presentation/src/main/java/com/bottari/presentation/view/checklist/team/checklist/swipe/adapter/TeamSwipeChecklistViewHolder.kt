package com.bottari.presentation.view.checklist.team.checklist.swipe.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bottari.presentation.databinding.ItemSwipeChecklistBinding
import com.bottari.presentation.model.TeamChecklistProductUiModel

class TeamSwipeChecklistViewHolder private constructor(
    private val binding: ItemSwipeChecklistBinding,
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(item: TeamChecklistProductUiModel) {
        binding.tvSwipeItemTitle.text = item.name
    }

    companion object {
        fun from(parent: ViewGroup): TeamSwipeChecklistViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = ItemSwipeChecklistBinding.inflate(inflater, parent, false)
            return TeamSwipeChecklistViewHolder(binding)
        }
    }
}
