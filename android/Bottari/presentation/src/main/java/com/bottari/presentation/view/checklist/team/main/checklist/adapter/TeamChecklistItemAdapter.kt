package com.bottari.presentation.view.checklist.team.main.checklist.adapter

import TeamChecklistTypeViewHolder
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bottari.presentation.model.TeamChecklistExpandableTypeUiModel
import com.bottari.presentation.model.TeamChecklistItem
import com.bottari.presentation.model.TeamChecklistProductUiModel

class TeamChecklistItemAdapter(
    private val teamChecklistEventListener: TeamChecklistEventListener,
) : ListAdapter<TeamChecklistItem, RecyclerView.ViewHolder>(DiffCallback) {
    override fun getItemViewType(position: Int): Int =
        when (getItem(position)) {
            is TeamChecklistExpandableTypeUiModel -> ITEM_VIEW_TYPE_TYPE
            is TeamChecklistProductUiModel -> ITEM_VIEW_TYPE_ITEM
        }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): RecyclerView.ViewHolder =
        when (viewType) {
            ITEM_VIEW_TYPE_TYPE -> {
                TeamChecklistTypeViewHolder.from(parent, teamChecklistEventListener)
            }

            ITEM_VIEW_TYPE_ITEM -> {
                TeamChecklistViewHolder.from(parent, teamChecklistEventListener)
            }

            else -> throw IllegalArgumentException(ERROR_VIEW_TYPE)
        }

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int,
    ) {
        val item = getItem(position)
        when {
            item is TeamChecklistExpandableTypeUiModel && holder is TeamChecklistTypeViewHolder -> {
                holder.bind(item)
            }

            item is TeamChecklistProductUiModel && holder is TeamChecklistViewHolder -> {
                holder.bind(item)
            }
        }
    }

    interface TeamChecklistEventListener :
        TeamChecklistTypeViewHolder.OnTeamChecklistTypeClickListener,
        TeamChecklistViewHolder.OnTeamChecklistItemClickListener

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
                        oldItem is TeamChecklistExpandableTypeUiModel && newItem is TeamChecklistExpandableTypeUiModel ->
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
