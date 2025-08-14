package com.bottari.presentation.view.checklist.team.checklist.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bottari.presentation.R
import com.bottari.presentation.model.TeamChecklistItemUiModel
import com.bottari.presentation.model.TeamChecklistRowUiModel
import com.bottari.presentation.model.TeamChecklistTypeUiModel
import com.bottari.presentation.view.checklist.team.checklist.ItemClickListener

class TeamChecklistItemAdapter(
    private val clickListener: ItemClickListener,
) : ListAdapter<TeamChecklistRowUiModel, RecyclerView.ViewHolder>(DiffCallback) {
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
        val item = getItem(position)
        when {
            item is TeamChecklistTypeUiModel && holder is TeamChecklistTypeViewHolder -> {
                holder.bind(item)
            }

            item is TeamChecklistItemUiModel && holder is TeamChecklistViewHolder -> {
                holder.bind(item)
            }
        }
    }

    companion object {
        const val ERROR_VIEW_TYPE = "잘못된 뷰 타입 입니다"

        private val DiffCallback =
            object : DiffUtil.ItemCallback<TeamChecklistRowUiModel>() {
                override fun areItemsTheSame(
                    oldItem: TeamChecklistRowUiModel,
                    newItem: TeamChecklistRowUiModel,
                ): Boolean =
                    when {
                        oldItem is TeamChecklistTypeUiModel && newItem is TeamChecklistTypeUiModel ->
                            oldItem.type == newItem.type

                        oldItem is TeamChecklistItemUiModel && newItem is TeamChecklistItemUiModel ->
                            oldItem.id == newItem.id

                        else -> false
                    }

                override fun areContentsTheSame(
                    oldItem: TeamChecklistRowUiModel,
                    newItem: TeamChecklistRowUiModel,
                ): Boolean = oldItem == newItem
            }
    }
}
