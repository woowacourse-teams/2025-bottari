package com.bottari.presentation.view.template.create.adapter

import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.text.TextPaint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bottari.presentation.R
import com.bottari.presentation.common.extension.dpToPx
import com.bottari.presentation.common.listener.OnItemClickListener
import com.bottari.presentation.databinding.ItemTemplateCreateMyBottariBinding
import com.bottari.presentation.model.MyBottariUiModel

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
        binding.tvTemplateCreateMyBottariItems.post {
            val context = binding.tvTemplateCreateMyBottariItems.context
            val paint = binding.tvTemplateCreateMyBottariItems.paint
            val width = binding.root.width
            val summary =
                generateSummaryTextFitting(
                    context = context,
                    paint = paint,
                    availableWidth = width - TEXT_PADDING_OFFSET_PX,
                    itemNames = bottari.items.map { item -> item.name },
                )
            binding.tvTemplateCreateMyBottariItems.text = summary
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

    private fun generateSummaryTextFitting(
        context: Context,
        paint: TextPaint,
        availableWidth: Int,
        itemNames: List<String>,
    ): String {
        if (itemNames.isEmpty()) return ""
        val fullText = itemNames.joinToString()
        if (paint.measureText(fullText) <= availableWidth) {
            return fullText
        }
        val totalItemCount = itemNames.size
        val suffix = context.getString(R.string.common_format_template_items, totalItemCount)
        val fittingNames =
            itemNames
                .indices
                .asSequence()
                .map { endIndex -> itemNames.subList(0, endIndex + 1) }
                .takeWhile { names ->
                    val text = names.joinToString() + suffix
                    paint.measureText(text) < availableWidth
                }.lastOrNull()
                ?: emptyList()
        val prefix = fittingNames.joinToString()
        return prefix + suffix
    }

    companion object {
        private const val TEXT_PADDING_OFFSET_PX = 50
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
