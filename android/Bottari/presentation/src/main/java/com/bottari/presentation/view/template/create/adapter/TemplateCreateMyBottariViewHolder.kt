package com.bottari.presentation.view.template.create.adapter

import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bottari.presentation.R
import com.bottari.presentation.common.extension.dpToPx
import com.bottari.presentation.common.listener.OnItemClickListener
import com.bottari.presentation.databinding.ItemTemplateCreateMyBottariBinding
import com.bottari.presentation.model.MyBottariUiModel
import com.bottari.presentation.util.ItemSummaryUtil.setSummaryItems

class TemplateCreateMyBottariViewHolder(
    private val binding: ItemTemplateCreateMyBottariBinding,
    listener: OnItemClickListener,
) : RecyclerView.ViewHolder(binding.root) {
    init {
        binding.root.setOnClickListener {
            listener.onItemClick(bindingAdapterPosition.toLong())
        }
    }

    fun bind(bottari: MyBottariUiModel) {
        updateSelectedStateUI(bottari.isSelected)
        binding.tvTemplateCreateMyBottariTitle.text = bottari.title

        val itemNames = bottari.items.map { it.name }
        val suffixText = itemView.context.getString(R.string.common_format_template_items)

        binding.tvTemplateCreateMyBottariItems.post {
            binding.tvTemplateCreateMyBottariItems.setSummaryItems(
                itemNames = itemNames,
                suffixFormat = suffixText,
            )
        }
    }

    private fun updateSelectedStateUI(isSelected: Boolean) {
        val background = binding.root.background.mutate()
        val alpha = if (isSelected) 1f else 0.5f
        binding.root.alpha = alpha

        if (background is GradientDrawable) {
            val colorRes = if (isSelected) R.color.primary else R.color.transparent
            val strokeColor = ContextCompat.getColor(itemView.context, colorRes)
            background.setStroke(
                itemView.context.dpToPx(SELECTED_STATE_STROKE_WIDTH_DP),
                strokeColor,
            )
        }

        val scale = if (isSelected) 1.2f else 1f

        binding.root.scaleX = scale
        binding.root.scaleY = scale
    }

    companion object {
        private const val SELECTED_STATE_STROKE_WIDTH_DP = 2

        fun from(
            parent: ViewGroup,
            listener: OnItemClickListener,
        ): TemplateCreateMyBottariViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = ItemTemplateCreateMyBottariBinding.inflate(inflater, parent, false)
            return TemplateCreateMyBottariViewHolder(binding, listener)
        }
    }
}
