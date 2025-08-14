package com.bottari.presentation.view.checklist.team.checklist.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bottari.presentation.R
import com.bottari.presentation.databinding.ItemTeamChecklistOptionBinding
import com.bottari.presentation.model.TeamChecklistCategoryUiModel
import com.bottari.presentation.model.TeamChecklistItemUiModel
import com.bottari.presentation.model.TeamChecklistRowUiModel

class TeamChecklistItemAdapter(
    private val onParentClick: (TeamChecklistCategoryUiModel) -> Unit,
    private val onChildClick: (TeamChecklistItemUiModel) -> Unit,
) : ListAdapter<TeamChecklistRowUiModel, RecyclerView.ViewHolder>(DiffCallback) {
    override fun getItemViewType(position: Int): Int =
        when (getItem(position)) {
            is TeamChecklistCategoryUiModel -> R.layout.item_team_checklist_option
            is TeamChecklistItemUiModel -> R.layout.item_team_checklist
        }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): RecyclerView.ViewHolder =
        when (viewType) {
            R.layout.item_team_checklist_option -> {
                val binding =
                    ItemTeamChecklistOptionBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false,
                    )
                TeamChecklistCategoryViewHolder(binding, onParentClick)
            }
            R.layout.item_team_checklist -> {
                TeamChecklistViewHolder.from(parent) { position ->
                    val item = getItem(position)
                    if (item is TeamChecklistItemUiModel) {
                        onChildClick(item)
                    }
                }
            }
            else -> throw IllegalArgumentException("Unknown viewType $viewType")
        }

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int,
    ) {
        when (val item = getItem(position)) {
            is TeamChecklistCategoryUiModel -> (holder as TeamChecklistCategoryViewHolder).bind(item)
            is TeamChecklistItemUiModel -> (holder as TeamChecklistViewHolder).bind(item)
        }
    }

    companion object {
        private val DiffCallback =
            object : DiffUtil.ItemCallback<TeamChecklistRowUiModel>() {
                override fun areItemsTheSame(
                    oldItem: TeamChecklistRowUiModel,
                    newItem: TeamChecklistRowUiModel,
                ): Boolean {
                    if (oldItem::class != newItem::class) return false
                    return when (oldItem) {
                        is TeamChecklistCategoryUiModel ->
                            (newItem as TeamChecklistCategoryUiModel).category == oldItem.category
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
