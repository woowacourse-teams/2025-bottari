package com.bottari.presentation.view.checklist.team.checklist.adapter

import androidx.recyclerview.widget.RecyclerView
import com.bottari.presentation.databinding.ItemTeamChecklistOptionBinding
import com.bottari.presentation.model.TeamChecklistTypeUiModel

class TeamChecklistTypeViewHolder(
    private val binding: ItemTeamChecklistOptionBinding,
    private val onParentClick: (TeamChecklistTypeUiModel) -> Unit,
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(teamChecklistParent: TeamChecklistTypeUiModel) {
        binding.tvChecklistItemTitle.text = teamChecklistParent.type.title
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
