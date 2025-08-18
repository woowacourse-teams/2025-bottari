package com.bottari.presentation.view.checklist.team.checklist.swipe.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.bottari.presentation.model.TeamChecklistProductUiModel
import java.util.Objects

class TeamSwipeChecklistAdapter : ListAdapter<TeamChecklistProductUiModel, TeamSwipeChecklistViewHolder>(DiffUtil) {
    override fun getItemId(position: Int): Long {
        val item = getItem(position)
        return Objects.hash(item.id, item.type).toLong()
    }

    override fun onBindViewHolder(
        holder: TeamSwipeChecklistViewHolder,
        position: Int,
    ) {
        holder.bind(getItem(position))
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): TeamSwipeChecklistViewHolder = TeamSwipeChecklistViewHolder.from(parent)

    companion object {
        private val DiffUtil =
            object : DiffUtil.ItemCallback<TeamChecklistProductUiModel>() {
                override fun areContentsTheSame(
                    oldItem: TeamChecklistProductUiModel,
                    newItem: TeamChecklistProductUiModel,
                ): Boolean = oldItem == newItem

                override fun areItemsTheSame(
                    oldItem: TeamChecklistProductUiModel,
                    newItem: TeamChecklistProductUiModel,
                ): Boolean = (oldItem.id == newItem.id && oldItem.type == newItem.type)
            }
    }
}
