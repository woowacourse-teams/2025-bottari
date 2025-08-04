package com.bottari.presentation.view.common.decoration

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ItemSpacingDecoration(
    private val spacing: Int,
    private val includeEdge: Boolean = false,
    private val onlyBetweenItems: Boolean = false,
) : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State,
    ) {
        val position = parent.getChildAdapterPosition(view)
        if (position == RecyclerView.NO_POSITION) return
        val layoutManager = parent.layoutManager
        if (layoutManager !is LinearLayoutManager) return
        val isVertical = layoutManager.orientation == LinearLayoutManager.VERTICAL
        val itemCount = state.itemCount
        when {
            onlyBetweenItems -> {
                if (position < itemCount - 1) {
                    if (isVertical) {
                        outRect.bottom = spacing
                    } else {
                        outRect.right = spacing
                    }
                }
            }

            includeEdge -> {
                if (isVertical) {
                    outRect.top = if (position == 0) spacing else spacing / 2
                    outRect.bottom = if (position == itemCount - 1) spacing else spacing / 2
                } else {
                    outRect.left = if (position == 0) spacing else spacing / 2
                    outRect.right = if (position == itemCount - 1) spacing else spacing / 2
                }
            }

            else -> {
                if (isVertical) {
                    outRect.top = if (position == 0) 0 else spacing
                } else {
                    outRect.left = if (position == 0) 0 else spacing
                }
            }
        }
    }
}
