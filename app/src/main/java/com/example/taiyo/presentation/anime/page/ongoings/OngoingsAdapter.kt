package com.example.taiyo.presentation.anime.page.ongoings

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.taiyo.R
import com.example.taiyo.databinding.ItemAnimeOngoingsBinding
import com.example.taiyo.domain.entity.Anime
import com.example.taiyo.presentation.anime.page.adapters.AnimeDiffCallback

class OngoingsAdapter(
    private val context: Context
) : PagingDataAdapter<Anime, ViewHolder>(AnimeDiffCallback) {
    var onAnimeClick: ((Anime) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemAnimeOngoingsBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return OngoingsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val anime = getItem(position)
        anime?.let {
            with ((holder as OngoingsViewHolder).binding) {
                with(it) {
                    Glide.with(context)
                        .load(it.image)
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .into(sivPoster)
                    tvTitle.text = it.title
                    tvEpisodesAired.text = it.episodesAired.toString()
                    tvEpisodesTotal.text = context.getString(R.string.ongoings_episodes, episodesTotal)
                    root.setOnClickListener { onAnimeClick?.invoke(this) }
                }
            }
        }
    }
}