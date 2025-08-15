package com.bottari.presentation.view.edit.team.item.personal.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.bottari.presentation.model.BottariItemUiModel

class TeamPersonalItemEditAdapter(
    private val eventListener: TeamPersonalItemEditEventListener,
) : ListAdapter<BottariItemUiModel, TeamPersonalItemEditViewHolder>(DiffUtil) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): TeamPersonalItemEditViewHolder = TeamPersonalItemEditViewHolder.from(parent, eventListener)

    override fun onBindViewHolder(
        holder: TeamPersonalItemEditViewHolder,
        position: Int,
    ) = holder.bind(getItem(position))

    interface TeamPersonalItemEditEventListener : TeamPersonalItemEditViewHolder.OnEditItemClickListener

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
