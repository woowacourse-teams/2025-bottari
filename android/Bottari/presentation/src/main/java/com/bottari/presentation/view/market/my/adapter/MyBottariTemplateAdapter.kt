package com.bottari.presentation.view.market.my.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.bottari.presentation.model.BottariTemplateUiModel
import com.bottari.presentation.view.market.my.listener.MyBottariTemplateEventListener

class MyBottariTemplateAdapter(
    private val eventListener: MyBottariTemplateEventListener,
) : ListAdapter<BottariTemplateUiModel, MyBottariTemplateViewHolder>(
        DiffUtil,
    ) {
    override fun onBindViewHolder(
        holder: MyBottariTemplateViewHolder,
        position: Int,
    ) {
        holder.bind(getItem(position))
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): MyBottariTemplateViewHolder = MyBottariTemplateViewHolder.from(parent, eventListener)

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
