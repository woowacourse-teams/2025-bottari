package com.bottari.presentation.common

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class BottomPaddingDecoration(
    private val bottomPadding: Int,
) : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State,
    ) {
        val isLastItem = parent.getChildAdapterPosition(view) == state.itemCount - 1
        if (isLastItem) {
            outRect.bottom = bottomPadding
        }
    }
}
