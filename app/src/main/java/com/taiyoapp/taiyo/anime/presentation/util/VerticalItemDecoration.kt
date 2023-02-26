package com.taiyoapp.taiyo.anime.presentation.util

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

class VerticalItemDecoration(
    private val spaceSize: Int,
    private val spanCount: Int,
    private val orientation: Int
) : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect, view: View,
        parent: RecyclerView, state: RecyclerView.State
    ) {
        val itemPosition = parent.getChildAdapterPosition(view)
        val itemCount = state.itemCount
        with(outRect) {
            if (orientation == GridLayoutManager.VERTICAL) {
                if (parent.getChildAdapterPosition(view) < spanCount) {
                    top = spaceSize + 16
                    bottom = spaceSize
                } else if (itemCount > 0 && itemPosition == itemCount - 1) {
                    top = spaceSize
                    bottom = spaceSize + 16
                } else {
                    top = spaceSize
                    bottom = spaceSize
                }
            } else {
                if (parent.getChildAdapterPosition(view) < spanCount) {
                    top = spaceSize + 16
                    bottom = spaceSize
                } else if (itemCount > 0 && itemPosition == itemCount - 1) {
                    top = spaceSize
                    bottom = spaceSize + 16
                } else {
                    top = spaceSize
                    bottom = spaceSize
                }
            }
            left = spaceSize
            right = spaceSize
        }
    }
}