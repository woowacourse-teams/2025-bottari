package com.bottari.presentation.view.checklist.team.checklist.adapter

import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bottari.presentation.R
import com.bottari.presentation.databinding.ItemTeamChecklistBinding
import com.bottari.presentation.model.TeamChecklistProductUiModel
import com.bottari.presentation.view.checklist.team.checklist.ItemClickListener

class TeamChecklistViewHolder private constructor(
    private val binding: ItemTeamChecklistBinding,
    private val clickListener: ItemClickListener,
) : RecyclerView.ViewHolder(binding.root) {
    private var currentItem: TeamChecklistProductUiModel? = null

    init {
        binding.root.setOnClickListener {
            currentItem?.let { item ->
                clickListener.onItemClick(item.id, item.type)
            }
        }
    }

    fun bind(item: TeamChecklistProductUiModel) {
        currentItem = item
        binding.ctvChecklistItemTitle.text = item.name
        updateCheckedState(item.isChecked)
    }

    private fun updateCheckedState(isChecked: Boolean) {
        val bgColorRes = if (isChecked) R.color.primary else R.color.white
        val bgColor = ContextCompat.getColor(itemView.context, bgColorRes)

        val textColorRes = if (isChecked) R.color.white else R.color.black
        val textColor = ContextCompat.getColor(itemView.context, textColorRes)

        binding.clChecklistItem.backgroundTintList = ColorStateList.valueOf(bgColor)
        binding.ctvChecklistItemTitle.setTextColor(textColor)
        binding.ctvChecklistItemTitle.isChecked = isChecked
    }

    companion object {
        fun from(
            parent: ViewGroup,
            itemClickListener: ItemClickListener,
        ): TeamChecklistViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = ItemTeamChecklistBinding.inflate(inflater, parent, false)
            return TeamChecklistViewHolder(binding, itemClickListener)
        }
    }
}
