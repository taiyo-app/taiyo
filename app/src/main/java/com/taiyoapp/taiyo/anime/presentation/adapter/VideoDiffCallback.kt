package com.taiyoapp.taiyo.anime.presentation.adapter

import androidx.recyclerview.widget.DiffUtil
import com.taiyoapp.taiyo.anime.domain.entity.Video

object VideoDiffCallback : DiffUtil.ItemCallback<Video>() {
    override fun areItemsTheSame(oldItem: Video, newItem: Video): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Video, newItem: Video): Boolean {
        return oldItem == newItem
    }
}