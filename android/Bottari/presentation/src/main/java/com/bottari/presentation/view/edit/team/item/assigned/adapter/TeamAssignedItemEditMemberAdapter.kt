package com.bottari.presentation.view.edit.team.item.assigned.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.bottari.presentation.model.TeamMemberUiModel

class TeamAssignedItemEditMemberAdapter(
    private val eventListener: TeamAssignedItemEditMemberEventListener,
) : ListAdapter<TeamMemberUiModel, TeamAssignedItemEditMemberViewHolder>(DiffUtil) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): TeamAssignedItemEditMemberViewHolder = TeamAssignedItemEditMemberViewHolder.from(parent, eventListener)

    override fun onBindViewHolder(
        holder: TeamAssignedItemEditMemberViewHolder,
        position: Int,
    ) = holder.bind(getItem(position))

    interface TeamAssignedItemEditMemberEventListener : TeamAssignedItemEditMemberViewHolder.OnMemberClickListener

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
                ): Boolean = oldItem.id == newItem.id
            }
    }
}
