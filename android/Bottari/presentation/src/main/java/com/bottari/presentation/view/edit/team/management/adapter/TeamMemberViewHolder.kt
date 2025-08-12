package com.bottari.presentation.view.edit.team.management.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bottari.presentation.databinding.ItemTeamMemberBinding

class TeamMemberViewHolder private constructor(
    private val binding: ItemTeamMemberBinding,
    private val hostName: String,
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(nickname: String) {
        binding.tvTeamMember.text = nickname
        if (nickname == hostName) binding.ivTeamHost.isVisible = true
    }

    companion object {
        fun from(
            parent: ViewGroup,
            hostName: String,
        ): TeamMemberViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = ItemTeamMemberBinding.inflate(inflater, parent, false)
            return TeamMemberViewHolder(binding, hostName)
        }
    }
}
