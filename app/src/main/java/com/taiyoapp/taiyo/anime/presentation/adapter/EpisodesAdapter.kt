package com.taiyoapp.taiyo.anime.presentation.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import androidx.viewbinding.ViewBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.taiyoapp.taiyo.R
import com.taiyoapp.taiyo.anime.domain.entity.Episodes.Episode
import com.taiyoapp.taiyo.anime.presentation.adapter.viewholder.EpisodeLargeViewHolder
import com.taiyoapp.taiyo.anime.presentation.adapter.viewholder.EpisodeMediumViewHolder
import com.taiyoapp.taiyo.anime.presentation.adapter.viewholder.EpisodeSmallViewHolder
import com.taiyoapp.taiyo.databinding.ItemEpisodeLargeBinding
import com.taiyoapp.taiyo.databinding.ItemEpisodeMediumBinding
import com.taiyoapp.taiyo.databinding.ItemEpisodeSmallBinding

class EpisodesAdapter(
    private val context: Context,
    private val kind: String
) : ListAdapter<Episode, ViewHolder>(EpisodesDiffCallback) {
    var onEpisodeClick: ((Episode) -> Unit)? = null

    var spanCount = 1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = getBindingFromViewType(viewType, parent)
        return getViewHolderFromBinding(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val episode = getItem(position)
        episode?.let {
            when (spanCount) {
                1 -> {
                    with((holder as EpisodeLargeViewHolder).binding) {
                        with(episode) {
                            Glide.with(context)
                                .load(screenshots[1])
                                .transition(DrawableTransitionOptions.withCrossFade())
                                .into(episodeImage)
                            if (kind == "Фильм") {
                                tvEpisodeNumber.text = "ФИЛЬМ"
                            } else {
                                tvEpisodeNumber.text = context.getString(
                                    R.string.episode_number,
                                    episodeNumber.toString()
                                )
                            }
                        }
                        root.setOnClickListener {
                            onEpisodeClick?.invoke(episode)
                        }
                    }
                }
                2 -> {
                    with((holder as EpisodeMediumViewHolder).binding) {
                        with(episode) {
                            Glide.with(context)
                                .load(screenshots[1])
                                .transition(DrawableTransitionOptions.withCrossFade())
                                .into(episodeImage)
                            tvEpisodeNumber.text = episodeNumber.toString()
                        }
                        root.setOnClickListener {
                            onEpisodeClick?.invoke(episode)
                        }
                    }
                }
                else -> {
                    with((holder as EpisodeSmallViewHolder).binding) {
                        with(episode) {
                            tvEpisodeNumber.text = episodeNumber.toString()
                        }
                        root.setOnClickListener {
                            onEpisodeClick?.invoke(episode)
                        }
                    }
                }
            }
        }
    }

    private fun getBindingFromViewType(viewType: Int, parent: ViewGroup): ViewBinding {
        val binding = when (viewType) {
            VIEW_TYPE_LARGE -> ItemEpisodeLargeBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
            VIEW_TYPE_MEDIUM -> ItemEpisodeMediumBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
            else -> ItemEpisodeSmallBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        }
        return binding
    }

    private fun getViewHolderFromBinding(binding: ViewBinding): ViewHolder {
        val viewHolder = when (binding) {
            is ItemEpisodeLargeBinding -> EpisodeLargeViewHolder(binding)
            is ItemEpisodeMediumBinding -> EpisodeMediumViewHolder(binding)
            else -> EpisodeSmallViewHolder(binding as ItemEpisodeSmallBinding)
        }
        return viewHolder
    }

    override fun getItemViewType(position: Int): Int {
        return when(spanCount) {
            1 -> VIEW_TYPE_LARGE
            2 -> VIEW_TYPE_MEDIUM
            else -> VIEW_TYPE_SMALL
        }
    }

    companion object {
        private const val VIEW_TYPE_LARGE = 1
        private const val VIEW_TYPE_MEDIUM = 2
        private const val VIEW_TYPE_SMALL = 3
    }
}