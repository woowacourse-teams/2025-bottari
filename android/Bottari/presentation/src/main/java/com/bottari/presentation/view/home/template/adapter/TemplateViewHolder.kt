package com.bottari.presentation.view.home.template.adapter

import android.content.Context
import android.text.TextPaint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bottari.presentation.R
import com.bottari.presentation.databinding.ItemTemplateBinding
import com.bottari.presentation.model.BottariTemplateUiModel
import com.bottari.presentation.view.home.template.listener.OnTemplateClickListener

class TemplateViewHolder private constructor(
    private val binding: ItemTemplateBinding,
    clickListener: OnTemplateClickListener,
) : RecyclerView.ViewHolder(binding.root) {
    private var bottariTemplateId: Long? = null

    init {
        binding.clBottariTemplate.setOnClickListener {
            bottariTemplateId?.let { clickListener.onTemplateClick(it) }
        }
    }

    fun bind(bottariTemplate: BottariTemplateUiModel) {
        bottariTemplateId = bottariTemplate.id
        binding.tvBottariTemplateAuthor.text = bottariTemplate.author
        binding.tvBottariTemplateTakenCount.text =
            itemView.context.getString(R.string.template_taken_count_prefix, bottariTemplate.takenCount)
        binding.tvBottariTemplateTitle.text = bottariTemplate.title
        binding.tvBottariTemplateItems.post {
            val context = binding.tvBottariTemplateItems.context
            val paint = binding.tvBottariTemplateItems.paint
            val width = binding.root.width
            val summary =
                generateSummaryTextFitting(
                    context = context,
                    paint = paint,
                    availableWidth = width - TEXT_PADDING_OFFSET_PX,
                    itemNames = bottariTemplate.items.map { item -> item.name },
                )
            binding.tvBottariTemplateItems.text = summary
        }
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
        private const val TEXT_PADDING_OFFSET_PX = 200

        fun from(
            parent: ViewGroup,
            clickListener: OnTemplateClickListener,
        ): TemplateViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = ItemTemplateBinding.inflate(inflater, parent, false)
            return TemplateViewHolder(binding, clickListener)
        }
    }
}
