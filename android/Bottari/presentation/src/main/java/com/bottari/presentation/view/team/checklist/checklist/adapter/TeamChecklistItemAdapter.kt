package com.bottari.presentation.view.team.checklist.checklist.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bottari.logger.BottariLogger
import com.bottari.presentation.databinding.ItemTeamChecklistOptionBinding
import com.bottari.presentation.model.TeamChecklistCategoryUiModel
import com.bottari.presentation.model.TeamChecklistItemUiModel
import com.bottari.presentation.model.TeamChecklistRowUiModel

class TeamChecklistItemAdapter(
    private val onParentClick: (TeamChecklistCategoryUiModel) -> Unit,
    private val onChildClick: (TeamChecklistItemUiModel) -> Unit,
) : ListAdapter<TeamChecklistRowUiModel, RecyclerView.ViewHolder>(DiffCallback) {
    inner class ParentViewHolder(
        private val binding: ItemTeamChecklistOptionBinding,
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(teamChecklistParent: TeamChecklistCategoryUiModel) {
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
            TeamChecklistItemType.CATEGORY -> {
                val binding =
                    ItemTeamChecklistOptionBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false,
                    )
                ParentViewHolder(binding)
            }

            TeamChecklistItemType.ITEM -> {
                TeamChecklistViewHolder.from(parent) { position ->
                    val item = currentList[position]
                    if (item is TeamChecklistItemUiModel) {
                        onChildClick(item)
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
            is TeamChecklistCategoryUiModel -> TeamChecklistItemType.CATEGORY
            is TeamChecklistItemUiModel -> TeamChecklistItemType.ITEM
        }

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int,
    ) {
        when (val item = getItem(position)) {
            is TeamChecklistCategoryUiModel -> (holder as ParentViewHolder).bind(item)
            is TeamChecklistItemUiModel -> (holder as TeamChecklistViewHolder).bind(item)
        }
    }

    companion object {
        private const val TOGGLE_SHAPE_UNOPENED = 0f
        private const val TOGGLE_SHAPE_OPENED = 90f

        private const val ERROR_MESSAGE_INVALID_VIEW_TYPE = "잘못된 뷰 타입입니다"

        private val DiffCallback =
            object : DiffUtil.ItemCallback<TeamChecklistRowUiModel>() {
                override fun areItemsTheSame(
                    oldItem: TeamChecklistRowUiModel,
                    newItem: TeamChecklistRowUiModel,
                ): Boolean {
                    if (oldItem::class != newItem::class) return false

                    return when (oldItem) {
                        is TeamChecklistCategoryUiModel -> {
                            (newItem as TeamChecklistCategoryUiModel).category ==
                                oldItem.category
                        }

                        is TeamChecklistItemUiModel -> {
                            val newTeamBottariItem = newItem as TeamChecklistItemUiModel
                            oldItem.id == newTeamBottariItem.id &&
                                oldItem.category == newTeamBottariItem.category
                        }
                    }
                }

                override fun areContentsTheSame(
                    oldItem: TeamChecklistRowUiModel,
                    newItem: TeamChecklistRowUiModel,
                ): Boolean = oldItem == newItem
            }
    }
}
