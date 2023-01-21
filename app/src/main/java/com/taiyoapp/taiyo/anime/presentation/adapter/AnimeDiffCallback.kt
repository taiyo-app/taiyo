package com.taiyoapp.taiyo.anime.presentation.adapter

import androidx.recyclerview.widget.DiffUtil
import com.taiyoapp.taiyo.anime.domain.entity.Anime

object AnimeDiffCallback : DiffUtil.ItemCallback<Anime>() {
    override fun areItemsTheSame(oldItem: Anime, newItem: Anime): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Anime, newItem: Anime): Boolean {
        return oldItem == newItem
    }
}