package com.taiyoapp.taiyo.anime.presentation.adapter

import androidx.recyclerview.widget.DiffUtil
import com.taiyoapp.taiyo.anime.domain.entity.EpisodeList.Episode

object EpisodeListDiffCallback: DiffUtil.ItemCallback<Episode>() {
    override fun areItemsTheSame(oldItem: Episode, newItem: Episode): Boolean {
        return oldItem.link == newItem.link
    }

    override fun areContentsTheSame(oldItem: Episode, newItem: Episode): Boolean {
        return oldItem == newItem
    }
}