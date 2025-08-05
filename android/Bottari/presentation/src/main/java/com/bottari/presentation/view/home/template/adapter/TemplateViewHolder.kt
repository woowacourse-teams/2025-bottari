package com.bottari.presentation.view.home.template.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bottari.presentation.R
import com.bottari.presentation.databinding.ItemTemplateBinding
import com.bottari.presentation.model.BottariTemplateUiModel
import com.bottari.presentation.util.ItemSummaryUtil.setSummaryItems
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

    fun bind(template: BottariTemplateUiModel) {
        bottariTemplateId = template.id
        binding.tvBottariTemplateAuthor.text = template.author
        binding.tvBottariTemplateTakenCount.text =
            itemView.context.getString(
                R.string.template_taken_count_prefix,
                template.takenCount,
            )

        val itemNames = template.items.map { it.name }
        val suffixText = itemView.context.getString(R.string.common_format_template_items)

        binding.tvBottariTemplateTitle.text = template.title
        binding.tvBottariTemplateItems.post {
            binding.tvBottariTemplateItems.setSummaryItems(
                itemNames = itemNames,
                suffixFormat = suffixText,
            )
        }
    }

    companion object {
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
