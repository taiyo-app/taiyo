package com.taiyoapp.taiyo.anime.presentation.util

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class HorizontalItemDecoration(
    private val spaceSize: Int,
    private val optionalSpaceSize: Int
) : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect, view: View,
        parent: RecyclerView, state: RecyclerView.State
    ) {
        val itemPosition = parent.getChildAdapterPosition(view)
        val itemCount = state.itemCount
        with(outRect) {
            if (parent.getChildAdapterPosition(view) == 0) {
                left = spaceSize + optionalSpaceSize
                right = spaceSize
            } else if (itemCount > 0 && itemPosition == itemCount - 1) {
                left = spaceSize
                right = spaceSize + optionalSpaceSize
            } else {
                left = spaceSize
                right = spaceSize
            }
            top = spaceSize
            bottom = spaceSize
        }
    }
}