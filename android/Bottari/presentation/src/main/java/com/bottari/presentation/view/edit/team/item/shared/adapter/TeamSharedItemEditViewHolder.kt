package com.bottari.presentation.view.edit.team.item.shared.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bottari.presentation.databinding.ItemEditPersonalItemBinding
import com.bottari.presentation.model.BottariItemUiModel

class TeamSharedItemEditViewHolder(
    private val binding: ItemEditPersonalItemBinding,
    eventListener: OnEditItemClickListener,
) : RecyclerView.ViewHolder(binding.root) {
    private var itemId: Long? = null

    init {
        binding.btnPersonalItemDelete.setOnClickListener {
            itemId?.let(eventListener::onClickDelete)
        }
    }

    fun bind(item: BottariItemUiModel) {
        itemId = item.id
        binding.tvPersonalItemName.text = item.name
    }

    interface OnEditItemClickListener {
        fun onClickDelete(itemId: Long)
    }

    companion object {
        fun from(
            parent: ViewGroup,
            eventListener: OnEditItemClickListener,
        ): TeamSharedItemEditViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = ItemEditPersonalItemBinding.inflate(layoutInflater, parent, false)
            return TeamSharedItemEditViewHolder(binding, eventListener)
        }
    }
}
