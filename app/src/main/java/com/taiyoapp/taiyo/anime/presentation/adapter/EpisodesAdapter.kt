package com.taiyoapp.taiyo.anime.presentation.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.taiyoapp.taiyo.R
import com.taiyoapp.taiyo.anime.domain.entity.Episodes.Episode
import com.taiyoapp.taiyo.anime.presentation.adapter.viewholder.EpisodeViewHolder
import com.taiyoapp.taiyo.databinding.ItemEpisodeLargeBinding

class EpisodesAdapter(
    private val context: Context,
) : ListAdapter<Episode, EpisodeViewHolder>(EpisodesDiffCallback) {
    var onEpisodeClick: ((Episode) -> Unit)? = null

    var episodeCounter = 1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EpisodeViewHolder {
        val binding = ItemEpisodeLargeBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return EpisodeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: EpisodeViewHolder, position: Int) {
        val episode = getItem(position)
        val episodeCount = position + 1
        with(holder.binding) {
            with(episode) {
                Glide.with(context)
                    .load(screenshots[1])
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(episodeImage)
                episodeNumber.text = context.getString(
                    R.string.episode_number,
                    episodeCount.toString()
                )
            }
            root.setOnClickListener {
                onEpisodeClick?.invoke(episode)
            }
        }
    }
}