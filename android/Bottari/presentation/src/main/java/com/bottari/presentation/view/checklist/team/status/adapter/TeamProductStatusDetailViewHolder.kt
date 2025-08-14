package com.bottari.presentation.view.checklist.team.status.adapter

import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bottari.presentation.R
import com.bottari.presentation.databinding.ItemChecklistMiniBinding
import com.bottari.presentation.model.MemberCheckStatusUiModel

class TeamProductStatusDetailViewHolder private constructor(
    private val binding: ItemChecklistMiniBinding,
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(item: MemberCheckStatusUiModel) {
        binding.tvChecklistItemMiniTitle.text = item.name
        setCheckedStatus(item.checked)
    }

    private fun setCheckedStatus(isChecked: Boolean) {
        if (isChecked) {
            binding.clChecklistItem.backgroundTintList =
                ColorStateList.valueOf(ContextCompat.getColor(binding.root.context, R.color.primary))
            return
        }
        binding.clChecklistItem.backgroundTintList =
            ColorStateList.valueOf(ContextCompat.getColor(binding.root.context, R.color.gray_787878))
    }

    companion object {
        fun from(parent: ViewGroup): TeamProductStatusDetailViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = ItemChecklistMiniBinding.inflate(inflater, parent, false)
            return TeamProductStatusDetailViewHolder(binding)
        }
    }
}
