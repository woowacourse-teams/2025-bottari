package com.bottari.presentation.view.edit.team.item.assigned.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.bottari.presentation.model.bottari.personal.SelectableItemUiModel

class TeamAssignedItemEditAdapter(
    private val eventListener: TeamAssignedItemEditEventListener,
) : ListAdapter<SelectableItemUiModel, TeamAssignedItemEditViewHolder>(DiffUtil) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): TeamAssignedItemEditViewHolder = TeamAssignedItemEditViewHolder.from(parent, eventListener)

    override fun onBindViewHolder(
        holder: TeamAssignedItemEditViewHolder,
        position: Int,
    ) = holder.bind(getItem(position))

    interface TeamAssignedItemEditEventListener : TeamAssignedItemEditViewHolder.AssignedItemEventListener

    companion object {
        private val DiffUtil =
            object : DiffUtil.ItemCallback<SelectableItemUiModel>() {
                override fun areContentsTheSame(
                    oldItem: SelectableItemUiModel,
                    newItem: SelectableItemUiModel,
                ): Boolean = oldItem == newItem

                override fun areItemsTheSame(
                    oldItem: SelectableItemUiModel,
                    newItem: SelectableItemUiModel,
                ): Boolean = oldItem.id == newItem.id && oldItem.isSelected == newItem.isSelected
            }
    }
}
