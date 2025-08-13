package com.bottari.presentation.view.team.checklist.checklist.adapter

import androidx.recyclerview.widget.RecyclerView
import com.bottari.presentation.databinding.ItemTeamChecklistOptionBinding
import com.bottari.presentation.model.TeamChecklistCategoryUiModel

class TeamChecklistCategoryViewHolder(
    private val binding: ItemTeamChecklistOptionBinding,
    private val onParentClick: (TeamChecklistCategoryUiModel) -> Unit,
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(teamChecklistParent: TeamChecklistCategoryUiModel) {
        binding.tvChecklistItemTitle.text = teamChecklistParent.category.title
        binding.ivChecklistOption.rotation =
            if (teamChecklistParent.isExpanded) TOGGLE_SHAPE_OPENED else TOGGLE_SHAPE_UNOPENED
        itemView.setOnClickListener {
            onParentClick(teamChecklistParent)
        }
    }

    companion object {
        private const val TOGGLE_SHAPE_UNOPENED = 0f
        private const val TOGGLE_SHAPE_OPENED = 90f
    }
}
