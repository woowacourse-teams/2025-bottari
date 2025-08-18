package com.bottari.presentation.view.checklist.team.main.status.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.bottari.presentation.model.MemberCheckStatusUiModel

class TeamBottariProductStatusDetailAdapter :
    ListAdapter<MemberCheckStatusUiModel, TeamBottariProductStatusDetailViewHolder>(
        DiffUtil,
    ) {
    override fun onBindViewHolder(
        holder: TeamBottariProductStatusDetailViewHolder,
        position: Int,
    ) {
        holder.bind(getItem(position))
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): TeamBottariProductStatusDetailViewHolder = TeamBottariProductStatusDetailViewHolder.from(parent)

    companion object {
        private val DiffUtil =
            object : DiffUtil.ItemCallback<MemberCheckStatusUiModel>() {
                override fun areContentsTheSame(
                    oldItem: MemberCheckStatusUiModel,
                    newItem: MemberCheckStatusUiModel,
                ): Boolean = oldItem == newItem

                override fun areItemsTheSame(
                    oldItem: MemberCheckStatusUiModel,
                    newItem: MemberCheckStatusUiModel,
                ): Boolean = oldItem.name == newItem.name
            }
    }
}
