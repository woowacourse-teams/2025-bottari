package com.bottari.presentation.view.edit.personal.item.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bottari.presentation.databinding.ItemEditPersonalItemBinding
import com.bottari.presentation.model.ItemUiModel
import com.bottari.presentation.view.edit.personal.item.listener.OnEditItemClickListener

class PersonalItemEditViewHolder(
    private val binding: ItemEditPersonalItemBinding,
    listener: OnEditItemClickListener,
) : RecyclerView.ViewHolder(binding.root) {
    private var itemId: Long? = null

    init {
        binding.btnPersonalItemDelete.setOnClickListener {
            itemId?.let { listener.onClick(it) }
        }
    }

    fun bind(item: ItemUiModel) {
        itemId = item.id
        binding.tvPersonalItemName.text = item.name
    }

    companion object {
        fun from(
            parent: ViewGroup,
            listener: OnEditItemClickListener,
        ): PersonalItemEditViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = ItemEditPersonalItemBinding.inflate(layoutInflater, parent, false)
            return PersonalItemEditViewHolder(binding, listener)
        }
    }
}
