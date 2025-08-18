package com.bottari.presentation.view.checklist.team.main.status.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bottari.presentation.R
import com.bottari.presentation.databinding.ItemTeamBottariItemStatusBinding
import com.bottari.presentation.model.TeamBottariProductStatusUiModel

class TeamBottariProductStatusViewHolder private constructor(
    private val binding: ItemTeamBottariItemStatusBinding,
    private val listener: OnTeamProductStatusItemClickListener,
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(
        item: TeamBottariProductStatusUiModel,
        isSelected: Boolean,
    ) {
        binding.root.setOnClickListener {
            listener.onItemClick(item)
        }
        binding.tvTeamItemName.text = item.name
        binding.tvTeamItemStatus.text =
            binding.root.context.getString(
                R.string.team_product_status_format,
                item.checkItemsCount,
                item.totalItemsCount,
            )
        updateSelectedState(isSelected)
    }

    private fun updateSelectedState(isSelected: Boolean) {
        binding.clTeamBottariItemStatus.setBackgroundResource(
            if (isSelected) {
                R.drawable.bg_white_radius_12dp_primary_boder
            } else {
                R.drawable.bg_white_radius_12dp
            },
        )
    }

    interface OnTeamProductStatusItemClickListener {
        fun onItemClick(item: TeamBottariProductStatusUiModel)
    }

    companion object {
        fun from(
            parent: ViewGroup,
            listener: OnTeamProductStatusItemClickListener,
        ): TeamBottariProductStatusViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = ItemTeamBottariItemStatusBinding.inflate(inflater, parent, false)
            return TeamBottariProductStatusViewHolder(binding, listener)
        }
    }
}
