package com.taiyoapp.taiyo.anime.presentation.adapter

import androidx.recyclerview.widget.DiffUtil
import com.taiyoapp.taiyo.anime.domain.entity.Screenshot

object ScreenshotsDiffCallback : DiffUtil.ItemCallback<Screenshot>() {
    override fun areItemsTheSame(oldItem: Screenshot, newItem: Screenshot): Boolean {
        return oldItem.original == newItem.original
    }

    override fun areContentsTheSame(oldItem: Screenshot, newItem: Screenshot): Boolean {
        return oldItem == newItem
    }
}