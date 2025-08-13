package com.bottari.presentation.view.edit.team.main.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bottari.presentation.R
import com.bottari.presentation.databinding.ItemChecklistMiniBinding
import com.bottari.presentation.model.BottariItemTypeUiModel
import com.bottari.presentation.model.BottariItemUiModel

class TeamBottariEditItemViewHolder private constructor(
    private val binding: ItemChecklistMiniBinding,
    private val bottariItemType: BottariItemTypeUiModel,
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(item: BottariItemUiModel) {
        binding.root.applyStyle()
        binding.tvChecklistItemMiniTitle.text = item.name
    }

    private fun View.applyStyle() {
        val backgroundColor =
            when (bottariItemType) {
                is BottariItemTypeUiModel.ASSIGNED -> R.color.secondary
                BottariItemTypeUiModel.PERSONAL -> R.color.primary
                BottariItemTypeUiModel.SHARED -> R.color.green
            }
        backgroundTintList = context.getColorStateList(backgroundColor)
    }

    companion object {
        fun from(
            parent: ViewGroup,
            bottariItemType: BottariItemTypeUiModel,
        ): TeamBottariEditItemViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = ItemChecklistMiniBinding.inflate(inflater, parent, false)
            return TeamBottariEditItemViewHolder(binding, bottariItemType)
        }
    }
}
