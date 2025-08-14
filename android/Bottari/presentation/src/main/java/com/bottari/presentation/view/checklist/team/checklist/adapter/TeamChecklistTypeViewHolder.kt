package com.bottari.presentation.view.checklist.team.checklist.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bottari.presentation.databinding.ItemTeamChecklistOptionBinding
import com.bottari.presentation.model.TeamChecklistTypeUiModel
import com.bottari.presentation.view.checklist.team.checklist.getStringResId

class TeamChecklistTypeViewHolder(
    private val binding: ItemTeamChecklistOptionBinding,
    private val clickListener: TeamChecklistItemAdapter.OnItemClickListener,
) : RecyclerView.ViewHolder(binding.root) {
    private var currentType: TeamChecklistTypeUiModel? = null

    init {
        binding.root.setOnClickListener {
            currentType?.let { item ->
                clickListener.onTypeClick(item)
            }
        }
    }

    fun bind(type: TeamChecklistTypeUiModel) {
        this.currentType = type
        binding.tvChecklistItemTitle.setText(type.type.getStringResId())
        binding.ivChecklistOption.rotation =
            if (type.isExpanded) TOGGLE_SHAPE_OPENED else TOGGLE_SHAPE_UNOPENED
    }

    companion object {
        private const val TOGGLE_SHAPE_UNOPENED = 0f
        private const val TOGGLE_SHAPE_OPENED = 90f

        fun from(
            parent: ViewGroup,
            onItemClickListener: TeamChecklistItemAdapter.OnItemClickListener,
        ): TeamChecklistTypeViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = ItemTeamChecklistOptionBinding.inflate(inflater, parent, false)
            return TeamChecklistTypeViewHolder(binding, onItemClickListener)
        }
    }
}
