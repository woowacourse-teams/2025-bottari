package com.bottari.presentation.view.checklist.team.status.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.recyclerview.widget.RecyclerView
import com.bottari.presentation.R
import com.bottari.presentation.databinding.ItemTeamBottariTypeBinding
import com.bottari.presentation.model.BottariItemTypeUiModel
import com.bottari.presentation.model.TeamChecklistTypeUiModel

class TeamProductTypeViewHolder private constructor(
    private val binding: ItemTeamBottariTypeBinding,
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(item: TeamChecklistTypeUiModel) {
        binding.tvTypeName.setText(item.type.getStringResId())
    }

    @StringRes
    private fun BottariItemTypeUiModel.getStringResId(): Int =
        when (this) {
            BottariItemTypeUiModel.SHARED -> R.string.team_checklist_type_shared_text
            is BottariItemTypeUiModel.ASSIGNED -> R.string.team_checklist_type_assigned_text
            BottariItemTypeUiModel.PERSONAL -> R.string.team_checklist_type_personal_text
        }

    companion object {
        fun from(parent: ViewGroup): TeamProductTypeViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = ItemTeamBottariTypeBinding.inflate(inflater, parent, false)
            return TeamProductTypeViewHolder(binding)
        }
    }
}
