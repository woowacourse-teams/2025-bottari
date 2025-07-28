package com.bottari.presentation.view.market.my.adapter

import android.content.Context
import android.text.TextPaint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bottari.presentation.R
import com.bottari.presentation.databinding.ItemMyBottariTemplateBinding
import com.bottari.presentation.model.BottariTemplateUiModel
import com.bottari.presentation.view.market.my.listener.MyBottariTemplateEventListener

class MyBottariTemplateViewHolder private constructor(
    private val binding: ItemMyBottariTemplateBinding,
    eventListener: MyBottariTemplateEventListener,
) : RecyclerView.ViewHolder(binding.root) {
    private var bottariTemplateId: Long? = null

    init {
        itemView.setOnClickListener {
            bottariTemplateId?.let { eventListener.onDetailClick(it) }
        }
        binding.btnBottariTemplateDelete.setOnClickListener {
            bottariTemplateId?.let { eventListener.onDeleteClick(it) }
        }
    }

    fun bind(bottariTemplate: BottariTemplateUiModel) {
        bottariTemplateId = bottariTemplate.id
        binding.tvBottariTemplateTitle.text = bottariTemplate.title
        binding.tvBottariTemplateItemsTitle.post {
            val context = binding.tvBottariTemplateItemsTitle.context
            val paint = binding.tvBottariTemplateItemsTitle.paint
            val width = binding.root.width
            val summary =
                generateSummaryTextFitting(
                    context = context,
                    paint = paint,
                    availableWidth = width - TEXT_PADDING_OFFSET_PX,
                    itemNames = bottariTemplate.items.map { item -> item.name },
                )
            binding.tvBottariTemplateItemsTitle.text = summary
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
        val suffix = context.getString(R.string.template_items_name_text, totalItemCount)
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
        private const val TEXT_PADDING_OFFSET_PX = 300

        fun from(
            parent: ViewGroup,
            eventListener: MyBottariTemplateEventListener,
        ): MyBottariTemplateViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = ItemMyBottariTemplateBinding.inflate(inflater, parent, false)
            return MyBottariTemplateViewHolder(binding, eventListener)
        }
    }
}
