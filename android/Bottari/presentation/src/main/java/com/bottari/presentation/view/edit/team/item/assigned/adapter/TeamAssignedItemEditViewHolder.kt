package com.bottari.presentation.view.edit.team.item.assigned.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bottari.presentation.databinding.ItemEditAssignedItemBinding
import com.bottari.presentation.model.BottariItemTypeUiModel
import com.bottari.presentation.model.BottariItemUiModel

class TeamAssignedItemEditViewHolder(
    private val binding: ItemEditAssignedItemBinding,
    eventListener: AssignedItemEventListener,
) : RecyclerView.ViewHolder(binding.root) {
    private var currentItem: BottariItemUiModel? = null

    init {
        binding.btnAssignedItemDelete.setOnClickListener {
            currentItem?.let { item -> eventListener.onClickDelete(item.id) }
        }
        binding.root.setOnClickListener {
            currentItem?.let { item -> eventListener.onClickAssignedItem(item.name, item.id) }
        }
    }

    fun bind(item: BottariItemUiModel) {
        currentItem = item
        binding.tvAssignedItemName.text = item.name

        if (item.type is BottariItemTypeUiModel.ASSIGNED) {
            binding.tvAssignedItemMemberNames.text = item.type.members.joinToString { member -> member.nickname }
        }
    }

    interface AssignedItemEventListener {
        fun onClickDelete(itemId: Long)

        fun onClickAssignedItem(
            itemName: String,
            itemId: Long,
        )
    }

    companion object {
        fun from(
            parent: ViewGroup,
            eventListener: AssignedItemEventListener,
        ): TeamAssignedItemEditViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = ItemEditAssignedItemBinding.inflate(layoutInflater, parent, false)
            return TeamAssignedItemEditViewHolder(binding, eventListener)
        }
    }
}
