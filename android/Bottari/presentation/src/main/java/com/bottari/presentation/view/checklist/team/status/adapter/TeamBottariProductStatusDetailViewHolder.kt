package com.bottari.presentation.view.checklist.team.status.adapter

import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bottari.presentation.R
import com.bottari.presentation.databinding.ItemChecklistMiniBinding
import com.bottari.presentation.model.MemberCheckStatusUiModel

class TeamBottariProductStatusDetailViewHolder private constructor(
    private val binding: ItemChecklistMiniBinding,
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(item: MemberCheckStatusUiModel) {
        binding.tvChecklistItemMiniTitle.text = item.name
        setCheckedStatus(item.checked)
    }

    private fun setCheckedStatus(isChecked: Boolean) {
        val bgColorRes = if (isChecked) R.color.primary else R.color.gray_787878
        val bgColor = ContextCompat.getColor(itemView.context, bgColorRes)
        binding.clChecklistItem.backgroundTintList = ColorStateList.valueOf(bgColor)
    }

    companion object {
        fun from(parent: ViewGroup): TeamBottariProductStatusDetailViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = ItemChecklistMiniBinding.inflate(inflater, parent, false)
            return TeamBottariProductStatusDetailViewHolder(binding)
        }
    }
}
