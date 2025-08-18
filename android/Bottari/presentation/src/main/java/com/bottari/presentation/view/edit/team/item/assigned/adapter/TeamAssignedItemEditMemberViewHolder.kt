package com.bottari.presentation.view.edit.team.item.assigned.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bottari.presentation.databinding.ItemTeamMemberChipBinding
import com.bottari.presentation.model.TeamMemberUiModel

class TeamAssignedItemEditMemberViewHolder(
    private val binding: ItemTeamMemberChipBinding,
    eventListener: OnMemberClickListener,
) : RecyclerView.ViewHolder(binding.root) {
    private var memberId: Long? = null

    init {
        binding.root.setOnClickListener {
            memberId?.let(eventListener::onClickMember)
        }
    }

    fun bind(member: TeamMemberUiModel) {
        memberId = member.id
        binding.tvTeamMemberNickname.text = member.nickname
        binding.tvTeamMemberNickname.isSelected = member.isHost
    }

    interface OnMemberClickListener {
        fun onClickMember(memberId: Long)
    }

    companion object {
        fun from(
            parent: ViewGroup,
            eventListener: OnMemberClickListener,
        ): TeamAssignedItemEditMemberViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = ItemTeamMemberChipBinding.inflate(layoutInflater, parent, false)
            return TeamAssignedItemEditMemberViewHolder(binding, eventListener)
        }
    }
}
