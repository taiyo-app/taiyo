package com.taiyoapp.taiyo.anime.presentation.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.taiyoapp.taiyo.anime.domain.entity.Screenshot
import com.taiyoapp.taiyo.anime.presentation.adapter.viewholder.ScreenshotsViewHolder
import com.taiyoapp.taiyo.databinding.ItemScreenshotBinding

class ScreenshotsAdapter(
    private val context: Context,
) : ListAdapter<Screenshot, ScreenshotsViewHolder>(ScreenshotsDiffCallback) {
    var onScreenshotClick: ((Int) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScreenshotsViewHolder {
        val binding = ItemScreenshotBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ScreenshotsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ScreenshotsViewHolder, position: Int) {
        val screenshot = getItem(position)
        with (holder.binding) {
            Glide.with(context)
                .load(screenshot.original)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(sivScreenshot)
            root.setOnClickListener { onScreenshotClick?.invoke(position) }
        }
    }
}