package com.bottari.presentation.view.checklist.team.status.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bottari.presentation.R
import com.bottari.presentation.databinding.ItemTeamBottariItemStatusBinding
import com.bottari.presentation.model.TeamProductStatusUiModel

class TeamProductStatusViewHolder private constructor(
    private val binding: ItemTeamBottariItemStatusBinding,
    private val listener: OnTeamProductStatusItemClickListener,
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(
        item: TeamProductStatusUiModel,
        isSelected: Boolean,
    ) {
        binding.root.setOnClickListener {
            listener.onItemClick(item)
        }
        binding.tvTeamItemName.text = item.name
        binding.tvTeamItemStatus.text =
            item.checkItemsCount.toString() + " / " + item.totalItemsCount.toString()
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
        fun onItemClick(item: TeamProductStatusUiModel)
    }

    companion object {
        fun from(
            parent: ViewGroup,
            listener: OnTeamProductStatusItemClickListener,
        ): TeamProductStatusViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = ItemTeamBottariItemStatusBinding.inflate(inflater, parent, false)
            return TeamProductStatusViewHolder(binding, listener)
        }
    }
}
