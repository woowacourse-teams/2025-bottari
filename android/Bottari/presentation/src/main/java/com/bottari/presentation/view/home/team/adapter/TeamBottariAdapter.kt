package com.bottari.presentation.view.home.team.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.bottari.presentation.model.BottariUiModel

class TeamBottariAdapter(
    private val bottariEventListener: TeamBottariViewHolder.BottariEventListener,
) : ListAdapter<BottariUiModel, TeamBottariViewHolder>(DiffUtil) {
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
            object : DiffUtil.ItemCallback<BottariUiModel>() {
                override fun areContentsTheSame(
                    oldItem: BottariUiModel,
                    newItem: BottariUiModel,
                ): Boolean = oldItem == newItem

                override fun areItemsTheSame(
                    oldItem: BottariUiModel,
                    newItem: BottariUiModel,
                ): Boolean = oldItem.title == newItem.title
            }
    }
}
