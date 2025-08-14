package com.bottari.presentation.view.edit.team.main.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.bottari.presentation.model.BottariItemTypeUiModel
import com.bottari.presentation.model.BottariItemUiModel

class TeamBottariEditItemAdapter(
    private val bottariItemType: BottariItemTypeUiModel,
) : ListAdapter<BottariItemUiModel, TeamBottariEditItemViewHolder>(
        DiffUtil,
    ) {
    override fun onBindViewHolder(
        holder: TeamBottariEditItemViewHolder,
        position: Int,
    ) {
        holder.bind(getItem(position))
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): TeamBottariEditItemViewHolder = TeamBottariEditItemViewHolder.from(parent, bottariItemType)

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
