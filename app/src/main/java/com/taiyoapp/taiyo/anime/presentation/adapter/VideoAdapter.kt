package com.taiyoapp.taiyo.anime.presentation.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.taiyoapp.taiyo.anime.domain.entity.Video
import com.taiyoapp.taiyo.anime.presentation.adapter.viewholder.VideoViewHolder
import com.taiyoapp.taiyo.databinding.ItemVideoBinding

class VideoAdapter(
    private val context: Context,
) : ListAdapter<Video, VideoViewHolder>(VideoDiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoViewHolder {
        val binding = ItemVideoBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return VideoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: VideoViewHolder, position: Int) {
        val video = getItem(position)
        with (holder.binding) {
            Glide.with(context)
                .load(video.imageUrl)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(sivVideo)
            tvKind.text = video.kind
        }
    }
}