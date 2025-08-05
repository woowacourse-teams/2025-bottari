package com.bottari.presentation.view.template.my.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bottari.presentation.R
import com.bottari.presentation.databinding.ItemMyTemplateBinding
import com.bottari.presentation.model.BottariTemplateUiModel
import com.bottari.presentation.util.ItemSummaryUtil.setSummaryItems
import com.bottari.presentation.view.template.my.listener.MyBottariTemplateEventListener

class MyTemplateViewHolder private constructor(
    private val binding: ItemMyTemplateBinding,
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

    fun bind(template: BottariTemplateUiModel) {
        bottariTemplateId = template.id
        binding.tvBottariTemplateTitle.text = template.title
        binding.tvBottariTemplateTakenCount.text =
            itemView.context.getString(
                R.string.template_taken_count_prefix,
                template.takenCount,
            )

        val itemNames = template.items.map { it.name }
        val suffixText = itemView.context.getString(R.string.common_format_template_items)

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
            eventListener: MyBottariTemplateEventListener,
        ): MyTemplateViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = ItemMyTemplateBinding.inflate(inflater, parent, false)
            return MyTemplateViewHolder(binding, eventListener)
        }
    }
}
