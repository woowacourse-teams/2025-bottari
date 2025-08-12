package com.bottari.presentation.view.team.checklist.checklist.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bottari.logger.BottariLogger
import com.bottari.presentation.databinding.ItemTeamChecklistOptionBinding
import com.bottari.presentation.model.BottariItemUiModel
import com.bottari.presentation.model.TeamChecklistParentUIModel

class TeamChecklistItemAdapter(
    private val onParentClick: (TeamChecklistParentUIModel) -> Unit,
    private val onChildClick: (BottariItemUiModel) -> Unit,
) : ListAdapter<Any, RecyclerView.ViewHolder>(DiffCallback) {
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
            is TeamChecklistParentUIModel -> TeamChecklistItemType.PARENT
            is BottariItemUiModel -> TeamChecklistItemType.CHILD
            else -> {
                val errorMessage =
                    ERROR_MESSAGE_INVALID_DATA_TYPE
                BottariLogger.error(errorMessage)
                throw IllegalArgumentException(errorMessage)
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
                TeamChecklistViewHolder.from(parent) { itemId ->
                    val item = currentList.find { it is BottariItemUiModel && it.id == itemId }
                    item?.let { onChildClick(it as BottariItemUiModel) }
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
        when (holder) {
            is ParentViewHolder -> holder.bind(getItem(position) as TeamChecklistParentUIModel)
            is TeamChecklistViewHolder -> holder.bind(getItem(position) as BottariItemUiModel)
        }
    }

    companion object {
        private const val TOGGLE_SHAPE_UNOPENED = 0f
        private const val TOGGLE_SHAPE_OPENED = 90f

        private const val ERROR_MESSAGE_INVALID_DATA_TYPE = "잘못된 데이터 타입입니다"
        private const val ERROR_MESSAGE_INVALID_VIEW_TYPE = "잘못된 뷰 타입입니다"

        @SuppressLint("DiffUtilEquals")
        private val DiffCallback =
            object : DiffUtil.ItemCallback<Any>() {
                override fun areItemsTheSame(
                    oldItem: Any,
                    newItem: Any,
                ): Boolean {
                    if (oldItem::class != newItem::class) return false

                    return when (oldItem) {
                        is TeamChecklistParentUIModel -> (newItem as TeamChecklistParentUIModel).category == oldItem.category
                        is BottariItemUiModel -> (newItem as BottariItemUiModel).id == oldItem.id
                        else -> false
                    }
                }

                override fun areContentsTheSame(
                    oldItem: Any,
                    newItem: Any,
                ): Boolean = oldItem == newItem
            }
    }
}
