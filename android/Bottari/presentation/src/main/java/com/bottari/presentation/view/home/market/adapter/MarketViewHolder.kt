package com.bottari.presentation.view.home.market.adapter

import android.content.Context
import android.text.TextPaint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bottari.presentation.R
import com.bottari.presentation.databinding.ItemBottariTemplateBinding
import com.bottari.presentation.model.BottariTemplateUiModel
import com.bottari.presentation.view.home.market.listener.OnBottariTemplateClickListener

class MarketViewHolder private constructor(
    private val binding: ItemBottariTemplateBinding,
    clickListener: OnBottariTemplateClickListener,
) : RecyclerView.ViewHolder(binding.root) {
    private var bottariTemplateId: Long? = null

    init {
        binding.clBottariTemplate.setOnClickListener {
            bottariTemplateId?.let { clickListener.onClick(it) }
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
                    availableWidth = width - 300,
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

        val suffix = context.getString(R.string.template_items_name_text, itemNames.size)

        val fittingNames =
            itemNames
                .runningFold(emptyList<String>()) { acc, item -> acc + item }
                .filter { it.isNotEmpty() }
                .takeWhile { names ->
                    val preview = names.joinToString(", ")
                    paint.measureText("$preview$suffix") < availableWidth
                }.lastOrNull()
                ?: emptyList()

        val prefix = fittingNames.joinToString(", ")
        return buildString {
            append(prefix)
            append(suffix)
        }
    }

    companion object {
        fun from(
            parent: ViewGroup,
            clickListener: OnBottariTemplateClickListener,
        ): MarketViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = ItemBottariTemplateBinding.inflate(inflater, parent, false)
            return MarketViewHolder(binding, clickListener)
        }
    }
}
