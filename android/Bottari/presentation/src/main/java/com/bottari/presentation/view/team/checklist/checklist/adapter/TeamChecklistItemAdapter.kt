package com.bottari.presentation.view.team.checklist.checklist.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bottari.logger.BottariLogger
import com.bottari.presentation.databinding.ItemTeamChecklistOptionBinding
import com.bottari.presentation.model.TeamBottariItemUIModel
import com.bottari.presentation.model.TeamChecklistParentUIModel

class TeamChecklistItemAdapter(
    private val onParentClick: (TeamChecklistParentUIModel) -> Unit,
    private val onChildClick: (TeamBottariItemUIModel) -> Unit,
) : ListAdapter<TeamChecklistItem, RecyclerView.ViewHolder>(DiffCallback) {
    inner class ParentViewHolder(
        private val binding: ItemTeamChecklistOptionBinding,
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(teamChecklistParent: TeamChecklistParentUIModel) {
            binding.tvChecklistItemTitle.text = teamChecklistParent.category.title
            binding.ivChecklistOption.rotation =
                if (teamChecklistParent.isExpanded) TOGGLE_SHAPE_OPENED else TOGGLE_SHAPE_UNOPENED
            itemView.setOnClickListener {
                onParentClick(teamChecklistParent)
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): RecyclerView.ViewHolder =
        when (viewType) {
            TeamChecklistItemType.PARENT -> {
                val binding =
                    ItemTeamChecklistOptionBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false,
                    )
                ParentViewHolder(binding)
            }

            TeamChecklistItemType.CHILD -> {
                TeamChecklistViewHolder.from(parent) { position ->
                    val item = currentList[position]
                    if (item is TeamChecklistItem.TeamBottariItem) {
                        onChildClick(item.teamBottariItem)
                    }
                }
            }

            else -> {
                val errorMessage = ERROR_MESSAGE_INVALID_VIEW_TYPE
                BottariLogger.error(errorMessage)
                throw IllegalArgumentException(errorMessage)
            }
        }

    override fun getItemViewType(position: Int): Int =
        when (getItem(position)) {
            is TeamChecklistItem.CategoryPage -> TeamChecklistItemType.PARENT
            is TeamChecklistItem.TeamBottariItem -> TeamChecklistItemType.CHILD
        }

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int,
    ) {
        when (val item = getItem(position)) {
            is TeamChecklistItem.CategoryPage -> (holder as ParentViewHolder).bind(item.teamChecklistParent)
            is TeamChecklistItem.TeamBottariItem -> (holder as TeamChecklistViewHolder).bind(item.teamBottariItem)
        }
    }

    companion object {
        private const val TOGGLE_SHAPE_UNOPENED = 0f
        private const val TOGGLE_SHAPE_OPENED = 90f

        private const val ERROR_MESSAGE_INVALID_VIEW_TYPE = "잘못된 뷰 타입입니다"

        @SuppressLint("DiffUtilEquals")
        private val DiffCallback =
            object : DiffUtil.ItemCallback<TeamChecklistItem>() {
                override fun areItemsTheSame(
                    oldItem: TeamChecklistItem,
                    newItem: TeamChecklistItem,
                ): Boolean {
                    if (oldItem::class != newItem::class) return false

                    return when (oldItem) {
                        is TeamChecklistItem.CategoryPage -> {
                            (newItem as TeamChecklistItem.CategoryPage).teamChecklistParent.category == oldItem.teamChecklistParent.category
                        }

                        is TeamChecklistItem.TeamBottariItem -> {
                            val newTeamBottariItem = newItem as TeamChecklistItem.TeamBottariItem
                            oldItem.teamBottariItem.id == newTeamBottariItem.teamBottariItem.id &&
                                oldItem.teamBottariItem.category == newTeamBottariItem.teamBottariItem.category
                        }
                    }
                }

                override fun areContentsTheSame(
                    oldItem: TeamChecklistItem,
                    newItem: TeamChecklistItem,
                ): Boolean = oldItem == newItem
            }
    }
}
