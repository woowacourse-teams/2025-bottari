package com.bottari.presentation.view.template.my.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.bottari.presentation.model.BottariTemplateUiModel
import com.bottari.presentation.view.template.my.listener.MyBottariTemplateEventListener

class MyTemplateAdapter(
    private val eventListener: MyBottariTemplateEventListener,
) : ListAdapter<BottariTemplateUiModel, MyTemplateViewHolder>(
        DiffUtil,
    ) {
    override fun onBindViewHolder(
        holder: MyTemplateViewHolder,
        position: Int,
    ) {
        holder.bind(getItem(position))
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): MyTemplateViewHolder = MyTemplateViewHolder.from(parent, eventListener)

    companion object {
        private val DiffUtil =
            object : DiffUtil.ItemCallback<BottariTemplateUiModel>() {
                override fun areContentsTheSame(
                    oldItem: BottariTemplateUiModel,
                    newItem: BottariTemplateUiModel,
                ): Boolean = oldItem == newItem

                override fun areItemsTheSame(
                    oldItem: BottariTemplateUiModel,
                    newItem: BottariTemplateUiModel,
                ): Boolean = oldItem.id == newItem.id
            }
    }
}
