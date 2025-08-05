package com.bottari.presentation.view.common.decoration

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
        val adapter = parent.adapter ?: return
        val position = parent.getChildAdapterPosition(view)
        val isLastItem = position != RecyclerView.NO_POSITION && position == adapter.itemCount - 1
        if (isLastItem) {
            outRect.bottom = bottomPadding
        } else {
            outRect.bottom = 0
        }
    }
}
