package com.bottari.presentation.view.checklist.team.checklist.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bottari.presentation.R
import com.bottari.presentation.model.TeamChecklistItemUiModel
import com.bottari.presentation.model.TeamChecklistRowUiModel
import com.bottari.presentation.model.TeamChecklistTypeUiModel

class TeamChecklistItemAdapter(
    private val clickListener: OnItemClickListener,
) : ListAdapter<TeamChecklistRowUiModel, RecyclerView.ViewHolder>(DiffCallback) {
    interface OnItemClickListener {
        fun onTypeClick(position: TeamChecklistTypeUiModel)

        fun onItemClick(position: TeamChecklistItemUiModel)
    }

    override fun getItemViewType(position: Int): Int =
        when (getItem(position)) {
            is TeamChecklistTypeUiModel -> R.layout.item_team_checklist_option
            is TeamChecklistItemUiModel -> R.layout.item_team_checklist
        }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): RecyclerView.ViewHolder =
        when (viewType) {
            R.layout.item_team_checklist_option -> {
                TeamChecklistTypeViewHolder.from(parent, clickListener)
            }

            R.layout.item_team_checklist -> {
                TeamChecklistViewHolder.from(parent, clickListener)
            }

            else -> throw IllegalArgumentException(ERROR_VIEW_TYPE)
        }

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int,
    ) {
        when (val item = getItem(position)) {
            is TeamChecklistTypeUiModel -> (holder as TeamChecklistTypeViewHolder).bind(item)
            is TeamChecklistItemUiModel -> (holder as TeamChecklistViewHolder).bind(item)
        }
    }

    companion object {
        const val ERROR_VIEW_TYPE = "잘못된 뷰 타입 입니다"

        private val DiffCallback =
            object : DiffUtil.ItemCallback<TeamChecklistRowUiModel>() {
                override fun areItemsTheSame(
                    oldItem: TeamChecklistRowUiModel,
                    newItem: TeamChecklistRowUiModel,
                ): Boolean {
                    if (oldItem::class != newItem::class) return false
                    return when (oldItem) {
                        is TeamChecklistTypeUiModel ->
                            (newItem as TeamChecklistTypeUiModel).type == oldItem.type

                        is TeamChecklistItemUiModel ->
                            (newItem as TeamChecklistItemUiModel).id == oldItem.id
                    }
                }

                override fun areContentsTheSame(
                    oldItem: TeamChecklistRowUiModel,
                    newItem: TeamChecklistRowUiModel,
                ): Boolean = oldItem == newItem
            }
    }
}
