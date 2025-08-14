package com.bottari.presentation.view.checklist.team.checklist.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bottari.presentation.model.TeamChecklistItem
import com.bottari.presentation.model.TeamChecklistProductUiModel
import com.bottari.presentation.model.TeamChecklistTypeUiModel

class TeamChecklistItemAdapter(
    private val teamChecklistTypeClickListener: TeamChecklistTypeViewHolder.OnTeamChecklistTypeClickListener,
    private val teamChecklistItemClickListener: TeamChecklistViewHolder.OnTeamChecklistItemClickListener,
) : ListAdapter<TeamChecklistItem, RecyclerView.ViewHolder>(DiffCallback) {
    override fun getItemViewType(position: Int): Int =
        when (getItem(position)) {
            is TeamChecklistTypeUiModel -> ITEM_VIEW_TYPE_TYPE
            is TeamChecklistProductUiModel -> ITEM_VIEW_TYPE_ITEM
        }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): RecyclerView.ViewHolder =
        when (viewType) {
            ITEM_VIEW_TYPE_TYPE -> {
                TeamChecklistTypeViewHolder.from(parent, teamChecklistTypeClickListener)
            }

            ITEM_VIEW_TYPE_ITEM -> {
                TeamChecklistViewHolder.from(parent, teamChecklistItemClickListener)
            }

            else -> throw IllegalArgumentException(ERROR_VIEW_TYPE)
        }

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int,
    ) {
        val item = getItem(position)
        when {
            item is TeamChecklistTypeUiModel && holder is TeamChecklistTypeViewHolder -> {
                holder.bind(item)
            }

            item is TeamChecklistProductUiModel && holder is TeamChecklistViewHolder -> {
                holder.bind(item)
            }
        }
    }

    companion object {
        const val ERROR_VIEW_TYPE = "잘못된 뷰 타입 입니다"
        private const val ITEM_VIEW_TYPE_TYPE = 0
        private const val ITEM_VIEW_TYPE_ITEM = 1

        private val DiffCallback =
            object : DiffUtil.ItemCallback<TeamChecklistItem>() {
                override fun areItemsTheSame(
                    oldItem: TeamChecklistItem,
                    newItem: TeamChecklistItem,
                ): Boolean =
                    when {
                        oldItem is TeamChecklistTypeUiModel && newItem is TeamChecklistTypeUiModel ->
                            oldItem.type == newItem.type

                        oldItem is TeamChecklistProductUiModel && newItem is TeamChecklistProductUiModel ->
                            oldItem.id == newItem.id

                        else -> false
                    }

                override fun areContentsTheSame(
                    oldItem: TeamChecklistItem,
                    newItem: TeamChecklistItem,
                ): Boolean = oldItem == newItem
            }
    }
}
