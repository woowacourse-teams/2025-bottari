package com.bottari.presentation.view.team.checklist.checklist.adapter

import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bottari.presentation.R
import com.bottari.presentation.databinding.ItemChecklistBinding
import com.bottari.presentation.model.TeamChecklistItemUIModel

class TeamChecklistViewHolder private constructor(
    private val binding: ItemChecklistBinding,
    private val onTeamChecklistItemClickListener: OnTeamChecklistItemClickListener,
) : RecyclerView.ViewHolder(binding.root) {
    init {
        itemView.setOnClickListener {
            onTeamChecklistItemClickListener.onClick(adapterPosition)
        }
    }

    fun bind(item: TeamChecklistItemUIModel) {
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
            onTeamChecklistItemClickListener: OnTeamChecklistItemClickListener,
        ): TeamChecklistViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = ItemChecklistBinding.inflate(inflater, parent, false)
            return TeamChecklistViewHolder(binding, onTeamChecklistItemClickListener)
        }
    }
}
