package com.taiyoapp.taiyo.anime.presentation.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.taiyoapp.taiyo.anime.domain.entity.Screenshot
import com.taiyoapp.taiyo.anime.presentation.adapter.viewholder.ScreenshotsVpViewHolder
import com.taiyoapp.taiyo.databinding.ItemScreenshotVpBinding

class ScreenshotsVpAdapter(
    private val context: Context,
) : ListAdapter<Screenshot, ScreenshotsVpViewHolder>(ScreenshotsDiffCallback) {

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScreenshotsVpViewHolder {
        val binding = ItemScreenshotVpBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        binding.tivScreenshot.setOnTouchListener { view, event ->
            var result = true
            //can scroll horizontally checks if there's still a part of the image
            //that can be scrolled until you reach the edge
            if (event.pointerCount >= 2 || view.canScrollHorizontally(1) && view.canScrollHorizontally(
                    -1)
            ) {
                //multi-touch event
                result = when (event.action) {
                    MotionEvent.ACTION_DOWN, MotionEvent.ACTION_MOVE -> {
                        // Disallow RecyclerView to intercept touch events.
                        parent.requestDisallowInterceptTouchEvent(true)
                        // Disable touch on view
                        false
                    }
                    MotionEvent.ACTION_UP -> {
                        // Allow RecyclerView to intercept touch events.
                        parent.requestDisallowInterceptTouchEvent(false)
                        true
                    }
                    else -> true
                }
            }
            result
        }
        return ScreenshotsVpViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ScreenshotsVpViewHolder, position: Int) {
        val screenshot = getItem(position)
        with(holder.binding) {
            Glide.with(context)
                .load(screenshot.original)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(tivScreenshot)
        }
    }
}
