package com.bottari.presentation.view.checklist.team.status.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bottari.presentation.model.TeamChecklistTypeUiModel
import com.bottari.presentation.model.TeamProductStatusItem
import com.bottari.presentation.model.TeamProductStatusUiModel

class TeamProductStatusAdapter(
    private val listener: TeamProductStatusViewHolder.OnTeamProductStatusItemClickListener,
) : ListAdapter<TeamProductStatusItem, RecyclerView.ViewHolder>(
        DiffUtil,
    ) {
    private var selectedPosition = 1

    override fun getItemViewType(position: Int): Int =
        when (getItem(position)) {
            is TeamProductStatusUiModel -> ITEM_VIEW_TYPE_TYPE
            is TeamChecklistTypeUiModel -> ITEM_VIEW_TYPE_ITEM
        }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): RecyclerView.ViewHolder =
        when (viewType) {
            ITEM_VIEW_TYPE_TYPE -> {
                TeamProductStatusViewHolder.from(parent, listener)
            }

            ITEM_VIEW_TYPE_ITEM -> {
                TeamProductTypeViewHolder.from(parent)
            }

            else -> throw IllegalArgumentException(ERROR_VIEW_TYPE)
        }

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int,
    ) {
        val item = getItem(position)
        when {
            item is TeamProductStatusUiModel && holder is TeamProductStatusViewHolder -> {
                holder.bind(item, position == selectedPosition)
            }

            item is TeamChecklistTypeUiModel && holder is TeamProductTypeViewHolder -> {
                holder.bind(item)
            }
        }
    }

    fun updateSelectedPosition(position: Int) {
        val previousPosition = selectedPosition
        selectedPosition = position
        notifyItemChanged(previousPosition)
        notifyItemChanged(selectedPosition)
    }

    companion object {
        private const val ERROR_VIEW_TYPE = "잘못된 뷰 타입 입니다"
        private const val ITEM_VIEW_TYPE_TYPE = 0
        private const val ITEM_VIEW_TYPE_ITEM = 1

        private val DiffUtil =
            object : DiffUtil.ItemCallback<TeamProductStatusItem>() {
                override fun areContentsTheSame(
                    oldItem: TeamProductStatusItem,
                    newItem: TeamProductStatusItem,
                ): Boolean = oldItem == newItem

                override fun areItemsTheSame(
                    oldItem: TeamProductStatusItem,
                    newItem: TeamProductStatusItem,
                ): Boolean = oldItem == newItem
            }
    }
}
