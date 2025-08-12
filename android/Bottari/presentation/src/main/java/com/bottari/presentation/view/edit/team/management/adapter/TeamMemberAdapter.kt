package com.bottari.presentation.view.edit.team.management.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.bottari.presentation.model.TeamMemberUiModel

class TeamMemberAdapter : ListAdapter<TeamMemberUiModel, TeamMemberViewHolder>(DiffUtil) {
    override fun onBindViewHolder(
        holder: TeamMemberViewHolder,
        position: Int,
    ) {
        holder.bind(getItem(position))
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): TeamMemberViewHolder = TeamMemberViewHolder.from(parent)

    companion object {
        private val DiffUtil =
            object : DiffUtil.ItemCallback<TeamMemberUiModel>() {
                override fun areContentsTheSame(
                    oldItem: TeamMemberUiModel,
                    newItem: TeamMemberUiModel,
                ): Boolean = oldItem == newItem

                override fun areItemsTheSame(
                    oldItem: TeamMemberUiModel,
                    newItem: TeamMemberUiModel,
                ): Boolean = oldItem.nickname == newItem.nickname
            }
    }
}
