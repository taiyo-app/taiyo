package com.example.taiyo.presentation.anime

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.example.taiyo.R
import com.example.taiyo.databinding.ItemAnimeOngoingBinding
import com.example.taiyo.domain.entity.Anime

class OngoingsAdapter(
    private val context: Context
) : PagingDataAdapter<Anime, ViewHolder>(AnimeDiffCallback) {
    var onAnimeClick: ((Anime) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemAnimeOngoingBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return OngoingViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val anime = getItem(position)
        anime?.let {
            with ((holder as OngoingViewHolder).binding) {
                with(it) {
                    Glide.with(context).load(it.image).into(sivPoster)
                    tvTitle.text = it.title
                    tvEpisodesAired.text = it.episodesAired.toString()
                    tvEpisodesTotal.text = context.getString(R.string.ongoings_episodes, episodesTotal)
                    root.setOnClickListener { onAnimeClick?.invoke(this) }
                }
            }
        }
    }
}