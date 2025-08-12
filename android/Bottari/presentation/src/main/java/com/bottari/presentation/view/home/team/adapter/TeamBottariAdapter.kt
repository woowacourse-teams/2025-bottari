package com.bottari.presentation.view.home.team.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.bottari.presentation.model.TeamBottariUiModel

class TeamBottariAdapter(
    private val bottariEventListener: TeamBottariViewHolder.BottariEventListener,
) : ListAdapter<TeamBottariUiModel, TeamBottariViewHolder>(DiffUtil) {
    override fun onBindViewHolder(
        holder: TeamBottariViewHolder,
        position: Int,
    ) {
        holder.bind(getItem(position))
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): TeamBottariViewHolder = TeamBottariViewHolder.from(parent, bottariEventListener)

    companion object {
        private val DiffUtil =
            object : DiffUtil.ItemCallback<TeamBottariUiModel>() {
                override fun areContentsTheSame(
                    oldItem: TeamBottariUiModel,
                    newItem: TeamBottariUiModel,
                ): Boolean = oldItem == newItem

                override fun areItemsTheSame(
                    oldItem: TeamBottariUiModel,
                    newItem: TeamBottariUiModel,
                ): Boolean = oldItem.id == newItem.id
            }
    }
}
