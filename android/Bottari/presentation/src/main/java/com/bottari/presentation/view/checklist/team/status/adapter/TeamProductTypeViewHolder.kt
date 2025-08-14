package com.bottari.presentation.view.checklist.team.status.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bottari.presentation.databinding.ItemChecklistMiniBinding
import com.bottari.presentation.databinding.ItemTeamBottariTypeBinding
import com.bottari.presentation.view.checklist.team.checklist.getStringResId
import com.bottari.presentation.view.checklist.team.status.MemberCheckStatusUiModel
import com.bottari.presentation.view.checklist.team.status.TeamChecklistTypeUiModel

class TeamProductTypeViewHolder private constructor(
    private val binding: ItemTeamBottariTypeBinding,
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(item: TeamChecklistTypeUiModel) {
        binding.tvTypeName.setText(item.type.getStringResId())
    }

    companion object {
        fun from(parent: ViewGroup): TeamProductTypeViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = ItemTeamBottariTypeBinding.inflate(inflater, parent, false)
            return TeamProductTypeViewHolder(binding)
        }
    }
}
