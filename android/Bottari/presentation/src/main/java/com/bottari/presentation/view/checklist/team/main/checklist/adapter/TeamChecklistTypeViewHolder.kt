package com.bottari.presentation.view.checklist.team.main.checklist.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bottari.presentation.R
import com.bottari.presentation.databinding.ItemTeamChecklistOptionBinding
import com.bottari.presentation.model.BottariItemTypeUiModel
import com.bottari.presentation.model.TeamChecklistExpandableTypeUiModel

class TeamChecklistTypeViewHolder(
    private val binding: ItemTeamChecklistOptionBinding,
    onTeamChecklistTypeClickListener: OnTeamChecklistTypeClickListener,
) : RecyclerView.ViewHolder(binding.root) {
    private var currentType: TeamChecklistExpandableTypeUiModel? = null

    init {
        binding.root.setOnClickListener {
            currentType?.let { item ->
                onTeamChecklistTypeClickListener.onItemTypeHeaderClick(item.type)
            }
        }
    }

    fun bind(type: TeamChecklistExpandableTypeUiModel) {
        currentType = type
        binding.tvTeamChecklistItemTitle.setText(type.type.getStringResId())
        binding.ivTeamChecklistOption.rotation =
            if (type.isExpanded) TOGGLE_SHAPE_OPENED else TOGGLE_SHAPE_UNOPENED
        binding.tvEmptyViewMessage.isVisible = type.isExpanded && type.teamChecklistItems.isEmpty()
    }

    @StringRes
    private fun BottariItemTypeUiModel.getStringResId(): Int =
        when (this) {
            BottariItemTypeUiModel.SHARED -> R.string.bottari_item_type_shared_text
            is BottariItemTypeUiModel.ASSIGNED -> R.string.bottari_item_type_assigned_text
            BottariItemTypeUiModel.PERSONAL -> R.string.bottari_item_type_personal_text
        }

    interface OnTeamChecklistTypeClickListener {
        fun onItemTypeHeaderClick(type: BottariItemTypeUiModel)
    }

    companion object {
        private const val TOGGLE_SHAPE_UNOPENED = 0f
        private const val TOGGLE_SHAPE_OPENED = 90f

        fun from(
            parent: ViewGroup,
            onTeamChecklistTypeClickListener: OnTeamChecklistTypeClickListener,
        ): TeamChecklistTypeViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = ItemTeamChecklistOptionBinding.inflate(inflater, parent, false)
            return TeamChecklistTypeViewHolder(binding, onTeamChecklistTypeClickListener)
        }
    }
}
