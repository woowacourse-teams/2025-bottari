package com.bottari.presentation.view.edit.team.management.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter

class TeamMemberAdapter(
    private val hostName: String,
) : ListAdapter<String, TeamMemberViewHolder>(DiffUtil) {
    override fun onBindViewHolder(
        holder: TeamMemberViewHolder,
        position: Int,
    ) {
        holder.bind(getItem(position))
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): TeamMemberViewHolder = TeamMemberViewHolder.from(parent, hostName)

    companion object {
        private val DiffUtil =
            object : DiffUtil.ItemCallback<String>() {
                override fun areContentsTheSame(
                    oldItem: String,
                    newItem: String,
                ): Boolean = oldItem == newItem

                override fun areItemsTheSame(
                    oldItem: String,
                    newItem: String,
                ): Boolean = oldItem == newItem
            }
    }
}
