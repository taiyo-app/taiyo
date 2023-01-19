package com.example.taiyo.presentation.anime.page.adapters

import androidx.recyclerview.widget.DiffUtil
import com.example.taiyo.domain.entity.Anime

object AnimeDiffCallback : DiffUtil.ItemCallback<Anime>() {
    override fun areItemsTheSame(oldItem: Anime, newItem: Anime): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Anime, newItem: Anime): Boolean {
        return oldItem == newItem
    }
}