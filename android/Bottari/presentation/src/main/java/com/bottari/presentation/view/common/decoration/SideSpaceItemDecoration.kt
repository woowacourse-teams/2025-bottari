package com.bottari.presentation.view.common.decoration

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class SideSpaceItemDecoration(
    private val sideSpaceRatio: Float,
) : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State,
    ) {
        val itemWidth = parent.getChildAt(FIRST_ITEM_POSITION)?.width ?: return
        val sideSpace = (itemWidth * sideSpaceRatio).toInt()

        val position = parent.getChildAdapterPosition(view)
        val itemCount = parent.adapter?.itemCount ?: return

        if (position == FIRST_ITEM_POSITION) {
            outRect.left = sideSpace
            return
        }

        if (position == itemCount - LAST_ITEM_POSITION) {
            outRect.right = sideSpace
        }
    }

    companion object {
        private const val FIRST_ITEM_POSITION = 0
        private const val LAST_ITEM_POSITION = 1
    }
}
