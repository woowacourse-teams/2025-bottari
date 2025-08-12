package com.bottari.presentation.view.edit.team.management.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bottari.presentation.databinding.ItemTeamMemberBinding
import com.bottari.presentation.model.TeamMemberUiModel

class TeamMemberViewHolder private constructor(
    private val binding: ItemTeamMemberBinding,
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(teamMember: TeamMemberUiModel) {
        binding.tvTeamMember.text = teamMember.nickname
        if (teamMember.isHost) binding.ivTeamHost.isVisible = true
    }

    companion object {
        fun from(parent: ViewGroup): TeamMemberViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = ItemTeamMemberBinding.inflate(inflater, parent, false)
            return TeamMemberViewHolder(binding)
        }
    }
}
