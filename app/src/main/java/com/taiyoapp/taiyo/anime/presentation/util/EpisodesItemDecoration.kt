package com.taiyoapp.taiyo.anime.presentation.util

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class EpisodesItemDecoration(
    private val spaceSize: Int,
    private val optionalSpaceSize: Int,
) : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect, view: View,
        parent: RecyclerView, state: RecyclerView.State,
    ) {
        val itemPosition = parent.getChildAdapterPosition(view)
        val itemCount = state.itemCount
        with(outRect) {
            if (parent.getChildAdapterPosition(view) == 0) {
                top = spaceSize - 8
                bottom = spaceSize - 24
            } else if (itemCount > 0 && itemPosition == itemCount - 1) {
                top = spaceSize - 24
                bottom = spaceSize - 8
            } else {
                top = spaceSize - 24
                bottom = spaceSize - 24
            }
            left = spaceSize
            right = spaceSize
        }
    }
}