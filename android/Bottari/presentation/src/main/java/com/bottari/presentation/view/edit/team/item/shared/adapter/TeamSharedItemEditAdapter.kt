package com.bottari.presentation.view.edit.team.item.shared.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.bottari.presentation.model.BottariItemUiModel

class TeamSharedItemEditAdapter(
    private val eventListener: TeamSharedItemEditEventListener,
) : ListAdapter<BottariItemUiModel, TeamSharedItemEditViewHolder>(DiffUtil) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): TeamSharedItemEditViewHolder = TeamSharedItemEditViewHolder.from(parent, eventListener)

    override fun onBindViewHolder(
        holder: TeamSharedItemEditViewHolder,
        position: Int,
    ) = holder.bind(getItem(position))

    interface TeamSharedItemEditEventListener : TeamSharedItemEditViewHolder.OnEditItemClickListener

    companion object {
        private val DiffUtil =
            object : DiffUtil.ItemCallback<BottariItemUiModel>() {
                override fun areContentsTheSame(
                    oldItem: BottariItemUiModel,
                    newItem: BottariItemUiModel,
                ): Boolean = oldItem == newItem

                override fun areItemsTheSame(
                    oldItem: BottariItemUiModel,
                    newItem: BottariItemUiModel,
                ): Boolean = oldItem.id == newItem.id
            }
    }
}
