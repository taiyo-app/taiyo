package com.taiyoapp.taiyo.anime.presentation.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.taiyoapp.taiyo.anime.domain.entity.Video
import com.taiyoapp.taiyo.anime.presentation.adapter.viewholder.VideoKindViewHolder
import com.taiyoapp.taiyo.anime.presentation.util.formatVideoKind
import com.taiyoapp.taiyo.databinding.ItemVideoKindBinding

class VideoKindAdapter(
    private val context: Context,
) : ListAdapter<Video, VideoKindViewHolder>(VideoDiffCallback) {
    var onVideoItemClick: ((Video) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoKindViewHolder {
        val binding = ItemVideoKindBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return VideoKindViewHolder(binding)
    }

    override fun onBindViewHolder(holder: VideoKindViewHolder, position: Int) {
        val videoItem = getItem(position)
        with (holder.binding) {
            Glide.with(context)
                .load(videoItem.imageUrl)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(sivVideo)
            tvKind.text = formatVideoKind(videoItem.kind)
            root.setOnClickListener { onVideoItemClick?.invoke(videoItem) }
        }
    }
}