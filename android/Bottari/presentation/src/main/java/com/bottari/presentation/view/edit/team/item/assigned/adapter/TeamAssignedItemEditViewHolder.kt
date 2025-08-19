package com.bottari.presentation.view.edit.team.item.assigned.adapter

import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat.getColor
import androidx.recyclerview.widget.RecyclerView
import com.bottari.presentation.R
import com.bottari.presentation.common.extension.dpToPx
import com.bottari.presentation.databinding.ItemEditAssignedItemBinding
import com.bottari.presentation.model.BottariItemTypeUiModel
import com.bottari.presentation.model.BottariItemUiModel

class TeamAssignedItemEditViewHolder(
    private val binding: ItemEditAssignedItemBinding,
    private val eventListener: AssignedItemEventListener,
) : RecyclerView.ViewHolder(binding.root) {
    private var currentItemId: Long? = null

    init {
        binding.btnAssignedItemDelete.setOnClickListener {
            currentItemId?.let(eventListener::onClickAssignedItemDelete)
        }
        binding.root.setOnClickListener {
            currentItemId?.let(eventListener::onClickAssignedItem)
        }
    }

    fun bind(item: BottariItemUiModel) {
        currentItemId = item.id
        binding.tvAssignedItemName.text = item.name
        handleSelectedState(item.isSelected)

        if (item.type is BottariItemTypeUiModel.ASSIGNED) {
            binding.tvAssignedItemMemberNames.text =
                item.type.members.joinToString { member -> member.nickname }
        }
    }

    private fun handleSelectedState(isSelected: Boolean) {
        val background = binding.root.background.mutate()
        if (background is GradientDrawable) {
            val colorRes = if (isSelected) R.color.primary else R.color.white
            val strokeColor = getColor(itemView.context, colorRes)
            background.setStroke(itemView.context.dpToPx(DUPLICATE_BORDER_WIDTH_DP), strokeColor)
        }
    }

    interface AssignedItemEventListener {
        fun onClickAssignedItemDelete(itemId: Long)

        fun onClickAssignedItem(itemId: Long)
    }

    companion object {
        private const val DUPLICATE_BORDER_WIDTH_DP = 2

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
