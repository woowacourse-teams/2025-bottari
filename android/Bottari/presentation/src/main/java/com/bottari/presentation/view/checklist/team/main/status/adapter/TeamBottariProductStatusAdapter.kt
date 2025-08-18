package com.bottari.presentation.view.checklist.team.main.status.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bottari.presentation.model.TeamBottariProductStatusUiModel
import com.bottari.presentation.model.TeamChecklistTypeUiModel
import com.bottari.presentation.model.TeamProductStatusItem

class TeamBottariProductStatusAdapter(
    private val listener: TeamBottariProductStatusViewHolder.OnTeamProductStatusItemClickListener,
) : ListAdapter<TeamProductStatusItem, RecyclerView.ViewHolder>(
        DiffUtil,
    ) {
    private var selectedPosition = 1

    fun updateSelectedPosition(position: Int) {
        val previousPosition = selectedPosition
        selectedPosition = position
        notifyItemChanged(previousPosition)
        notifyItemChanged(selectedPosition)
    }

    override fun getItemViewType(position: Int): Int =
        when (getItem(position)) {
            is TeamBottariProductStatusUiModel -> ITEM_VIEW_TYPE_TYPE
            is TeamChecklistTypeUiModel -> ITEM_VIEW_TYPE_ITEM
        }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): RecyclerView.ViewHolder =
        when (viewType) {
            ITEM_VIEW_TYPE_TYPE -> {
                TeamBottariProductStatusViewHolder.from(parent, listener)
            }

            ITEM_VIEW_TYPE_ITEM -> {
                TeamBottariProductTypeViewHolder.from(parent)
            }

            else -> throw IllegalArgumentException(ERROR_VIEW_TYPE)
        }

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int,
    ) {
        val item = getItem(position)
        when {
            item is TeamBottariProductStatusUiModel && holder is TeamBottariProductStatusViewHolder -> {
                holder.bind(item, position == selectedPosition)
            }

            item is TeamChecklistTypeUiModel && holder is TeamBottariProductTypeViewHolder -> {
                holder.bind(item)
            }
        }
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
