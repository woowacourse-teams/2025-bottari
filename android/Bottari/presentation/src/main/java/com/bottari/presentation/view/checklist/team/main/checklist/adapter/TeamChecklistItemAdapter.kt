package com.bottari.presentation.view.checklist.team.main.checklist.adapter

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
            ITEM_VIEW_TYPE_TYPE -> TeamChecklistTypeViewHolder.from(parent, teamChecklistEventListener)
            ITEM_VIEW_TYPE_ITEM -> TeamChecklistViewHolder.from(parent, teamChecklistEventListener)
            else -> throw IllegalArgumentException(ERROR_VIEW_TYPE)
        }

    // 🔥 [수정됨] 페이로드를 처리하는 onBindViewHolder 추가
    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int,
        payloads: MutableList<Any>,
    ) {
        if (payloads.isNotEmpty() && holder is TeamChecklistTypeViewHolder) {
            // 페이로드가 있으면 부분 업데이트를 수행합니다.
            // 여기서는 isExpanded 상태(Boolean)를 페이로드로 전달받았다고 가정합니다.
            val isExpanded = payloads[0] as? Boolean ?: return
            holder.animateToggle(isExpanded)
        } else {
            // 페이로드가 없으면 전체 업데이트를 수행합니다. (기본 동작)
            super.onBindViewHolder(holder, position, payloads)
        }
    }

    // 이 함수는 전체 업데이트 시에만 호출됩니다.
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
