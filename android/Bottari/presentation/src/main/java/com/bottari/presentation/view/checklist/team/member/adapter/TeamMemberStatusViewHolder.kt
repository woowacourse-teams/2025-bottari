package com.bottari.presentation.view.checklist.team.member.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bottari.presentation.R
import com.bottari.presentation.databinding.ItemTeamMemberStatusBinding
import com.bottari.presentation.model.ChecklistItemUiModel
import com.bottari.presentation.model.TeamMemberStatusUiModel
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent

class TeamMemberStatusViewHolder(
    private val binding: ItemTeamMemberStatusBinding,
) : RecyclerView.ViewHolder(binding.root) {
    private val sharedItemAdapter: SharedItemAdapter by lazy { SharedItemAdapter() }
    private val assignedItemAdapter: AssignedItemAdapter by lazy { AssignedItemAdapter() }

    init {
        itemView.setOnClickListener {
            binding.groupItems.apply { isVisible = !isVisible }
        }
    }

    fun bind(status: TeamMemberStatusUiModel) {
        itemView.isClickable = status.isItemsEmpty.not()
        binding.tvMemberNickname.text = status.member.nickname
        binding.ivTeamHost.isVisible = status.member.isHost
        handleItemsStatus(status)
        setupSharedItems(status.sharedItems)
        setupAssignedItems(status.assignedItems)
    }

    private fun handleItemsStatus(status: TeamMemberStatusUiModel) {
        if (status.isItemsEmpty) {
            binding.tvItemsCountStatus.text =
                itemView.context.getString(R.string.team_members_status_items_empty_text)
            return
        }
        binding.tvItemsCountStatus.text =
            itemView.context.getString(
                R.string.team_members_status_items_count_text,
                status.checkedItemsCount,
                status.totalItemsCount,
            )
    }

    private fun setupSharedItems(sharedItems: List<ChecklistItemUiModel>) {
        binding.rvSharedItems.adapter = sharedItemAdapter
        binding.rvSharedItems.layoutManager = createFlexboxLayoutManager()
        sharedItemAdapter.submitList(sharedItems)
    }

    private fun setupAssignedItems(assignedItems: List<ChecklistItemUiModel>) {
        binding.rvAssignedItems.adapter = assignedItemAdapter
        binding.rvAssignedItems.layoutManager = createFlexboxLayoutManager()
        assignedItemAdapter.submitList(assignedItems)
    }

    private fun createFlexboxLayoutManager(): FlexboxLayoutManager =
        FlexboxLayoutManager(itemView.context).apply {
            flexDirection = FlexDirection.ROW
            flexWrap = FlexWrap.WRAP
            justifyContent = JustifyContent.FLEX_START
        }

    companion object {
        fun from(parent: ViewGroup): TeamMemberStatusViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = ItemTeamMemberStatusBinding.inflate(inflater, parent, false)
            return TeamMemberStatusViewHolder(binding)
        }
    }
}
