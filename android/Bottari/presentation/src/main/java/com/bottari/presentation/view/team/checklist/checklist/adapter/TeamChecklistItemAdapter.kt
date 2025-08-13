package com.bottari.presentation.view.team.checklist.checklist.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bottari.logger.BottariLogger
import com.bottari.presentation.databinding.ItemTeamChecklistOptionBinding
import com.bottari.presentation.model.TeamBottariItemUiModel
import com.bottari.presentation.model.TeamChecklistParentUIModel

class TeamChecklistItemAdapter(
    private val onParentClick: (TeamChecklistParentUIModel) -> Unit,
    private val onChildClick: (TeamBottariItemUiModel) -> Unit,
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

    override fun getItemViewType(position: Int): Int =
        when (getItem(position)) {
            is TeamChecklistItem.Parent -> TeamChecklistItemType.PARENT
            is TeamChecklistItem.Child -> TeamChecklistItemType.CHILD
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
                TeamChecklistViewHolder.from(parent) { itemId ->
                    val item = currentList.find { it is TeamChecklistItem.Child && it.teamBottariItem.id == itemId }
                    item?.let { onChildClick((it as TeamChecklistItem.Child).teamBottariItem) }
                }
            }

            else -> {
                val errorMessage = ERROR_MESSAGE_INVALID_VIEW_TYPE
                BottariLogger.error(errorMessage)
                throw IllegalArgumentException(errorMessage)
            }
        }

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int,
    ) {
        when (val item = getItem(position)) {
            is TeamChecklistItem.Parent -> (holder as ParentViewHolder).bind(item.teamChecklistParent)
            is TeamChecklistItem.Child -> (holder as TeamChecklistViewHolder).bind(item.teamBottariItem)
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
                        is TeamChecklistItem.Parent -> {
                            (newItem as TeamChecklistItem.Parent).teamChecklistParent.category == oldItem.teamChecklistParent.category
                        }
                        is TeamChecklistItem.Child -> {
                            val newChild = newItem as TeamChecklistItem.Child
                            oldItem.teamBottariItem.id == newChild.teamBottariItem.id && oldItem.teamBottariItem.category == newChild.teamBottariItem.category
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