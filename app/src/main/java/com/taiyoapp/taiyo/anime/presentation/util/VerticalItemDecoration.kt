package com.taiyoapp.taiyo.anime.presentation.util

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class VerticalItemDecoration(
    private val spaceSize: Int,
    private val spanCount: Int,
) : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect, view: View,
        parent: RecyclerView, state: RecyclerView.State
    ) {
        val itemPosition = parent.getChildAdapterPosition(view)
        val itemCount = state.itemCount
        with(outRect) {
            if (parent.getChildAdapterPosition(view) < spanCount) {
                top = spaceSize + 16
                bottom = spaceSize
            } else if (itemCount > 0 && itemPosition == itemCount - 1) {
                top = spaceSize
                bottom = spaceSize + 16
            } else {
                top = spaceSize + 8
                bottom = spaceSize + 8
            }
            left = spaceSize + 6
            right = spaceSize + 6
        }
    }
}