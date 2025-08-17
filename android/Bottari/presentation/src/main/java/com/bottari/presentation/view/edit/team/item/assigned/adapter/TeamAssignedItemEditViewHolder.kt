package com.bottari.presentation.view.edit.team.item.assigned.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bottari.presentation.databinding.ItemEditAssignedItemBinding
import com.bottari.presentation.model.BottariItemTypeUiModel
import com.bottari.presentation.model.BottariItemUiModel

class TeamAssignedItemEditViewHolder(
    private val binding: ItemEditAssignedItemBinding,
    eventListener: OnEditItemClickListener,
) : RecyclerView.ViewHolder(binding.root) {
    private var itemId: Long? = null

    init {
        binding.btnAssignedItemDelete.setOnClickListener {
            itemId?.let(eventListener::onClickDelete)
        }
    }

    fun bind(item: BottariItemUiModel) {
        itemId = item.id
        binding.tvAssignedItemName.text = item.name

        if (item.type is BottariItemTypeUiModel.ASSIGNED) {
            binding.tvAssignedItemMemberNames.text = item.type.members.joinToString()
        }
    }

    interface OnEditItemClickListener {
        fun onClickDelete(itemId: Long)
    }

    companion object {
        fun from(
            parent: ViewGroup,
            eventListener: OnEditItemClickListener,
        ): TeamAssignedItemEditViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = ItemEditAssignedItemBinding.inflate(layoutInflater, parent, false)
            return TeamAssignedItemEditViewHolder(binding, eventListener)
        }
    }
}
