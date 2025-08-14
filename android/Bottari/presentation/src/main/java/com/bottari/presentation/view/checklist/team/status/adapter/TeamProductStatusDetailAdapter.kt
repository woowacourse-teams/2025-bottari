package com.bottari.presentation.view.checklist.team.status.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.bottari.presentation.model.MemberCheckStatusUiModel

class TeamProductStatusDetailAdapter :
    ListAdapter<MemberCheckStatusUiModel, TeamProductStatusDetailViewHolder>(
        DiffUtil,
    ) {
    override fun onBindViewHolder(
        holder: TeamProductStatusDetailViewHolder,
        position: Int,
    ) {
        holder.bind(getItem(position))
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): TeamProductStatusDetailViewHolder = TeamProductStatusDetailViewHolder.from(parent)

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
