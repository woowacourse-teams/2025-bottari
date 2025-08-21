package com.bottari.presentation.view.checklist.team.main.member.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bottari.presentation.R
import com.bottari.presentation.databinding.ItemTeamMemberStatusBinding
import com.bottari.presentation.model.TeamMemberStatusUiModel
import com.bottari.presentation.model.TeamMemberUiModel
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent

class TeamMemberStatusViewHolder private constructor(
    memberStatusClickListener: MemberStatusClickListener,
    private val binding: ItemTeamMemberStatusBinding,
) : RecyclerView.ViewHolder(binding.root) {
    private val sharedItemAdapter: SharedItemAdapter by lazy { SharedItemAdapter() }
    private val assignedItemAdapter: AssignedItemAdapter by lazy { AssignedItemAdapter() }
    private var memberStatus: TeamMemberStatusUiModel? = null

    init {
        itemView.setOnClickListener {
            memberStatus?.member?.id?.let(memberStatusClickListener::onClickMember)
        }
        binding.btnHurryUpAlert.setOnClickListener {
            memberStatus?.member?.let(memberStatusClickListener::onClickSendRemind)
        }
        setupSharedItems()
        setupAssignedItems()
    }

    fun bind(status: TeamMemberStatusUiModel) {
        memberStatus = status
        itemView.isClickable = status.isItemsEmpty.not()
        binding.tvMemberNickname.text = status.member.nickname
        binding.ivTeamHost.isVisible = status.member.isHost
        handleItemsStatus(status)
        handleItemsCountStatus(status)
        binding.btnHurryUpAlert.isVisible = status.shouldHurryUp && status.isExpanded
        sharedItemAdapter.submitList(status.sharedItems)
        assignedItemAdapter.submitList(status.assignedItems)
    }

    private fun handleItemsCountStatus(status: TeamMemberStatusUiModel) {
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

    private fun handleItemsStatus(status: TeamMemberStatusUiModel) {
        binding.apply {
            rvSharedItems.isVisible = status.isExpanded
            rvAssignedItems.isVisible = status.isExpanded
            mdItems.isVisible = status.isExpanded
            btnHurryUpAlert.isVisible = status.isExpanded
        }
    }

    private fun setupSharedItems() {
        binding.rvSharedItems.adapter = sharedItemAdapter
        binding.rvSharedItems.layoutManager = createFlexboxLayoutManager()
    }

    private fun setupAssignedItems() {
        binding.rvAssignedItems.adapter = assignedItemAdapter
        binding.rvAssignedItems.layoutManager = createFlexboxLayoutManager()
    }

    private fun createFlexboxLayoutManager(): FlexboxLayoutManager =
        FlexboxLayoutManager(itemView.context).apply {
            flexDirection = FlexDirection.ROW
            flexWrap = FlexWrap.WRAP
            justifyContent = JustifyContent.FLEX_START
        }

    companion object {
        fun from(
            parent: ViewGroup,
            memberStatusClickListener: MemberStatusClickListener,
        ): TeamMemberStatusViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = ItemTeamMemberStatusBinding.inflate(inflater, parent, false)
            return TeamMemberStatusViewHolder(memberStatusClickListener, binding)
        }
    }

    interface MemberStatusClickListener {
        fun onClickMember(id: Long)

        fun onClickSendRemind(member: TeamMemberUiModel)
    }
}
