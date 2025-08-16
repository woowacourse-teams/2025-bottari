package com.bottari.presentation.view.checklist.team.member.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.bottari.presentation.model.TeamMemberStatusUiModel

class TeamMemberStatusAdapter(
    private val onRemindClickListener: TeamMemberStatusViewHolder.OnRemindClickListener,
) : ListAdapter<TeamMemberStatusUiModel, TeamMemberStatusViewHolder>(DiffUtil) {
    override fun onBindViewHolder(
        holder: TeamMemberStatusViewHolder,
        position: Int,
    ) {
        holder.bind(getItem(position))
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): TeamMemberStatusViewHolder = TeamMemberStatusViewHolder.from(parent, onRemindClickListener)

    companion object {
        private val DiffUtil =
            object : DiffUtil.ItemCallback<TeamMemberStatusUiModel>() {
                override fun areContentsTheSame(
                    oldItem: TeamMemberStatusUiModel,
                    newItem: TeamMemberStatusUiModel,
                ): Boolean = oldItem == newItem

                override fun areItemsTheSame(
                    oldItem: TeamMemberStatusUiModel,
                    newItem: TeamMemberStatusUiModel,
                ): Boolean = oldItem.member.nickname == newItem.member.nickname
            }
    }
}
