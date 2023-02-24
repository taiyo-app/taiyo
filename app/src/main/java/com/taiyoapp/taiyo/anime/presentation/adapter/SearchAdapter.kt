package com.taiyoapp.taiyo.anime.presentation.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import androidx.viewbinding.ViewBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.taiyoapp.taiyo.R
import com.taiyoapp.taiyo.anime.domain.entity.Anime
import com.taiyoapp.taiyo.anime.presentation.adapter.viewholder.AnonsViewHolder
import com.taiyoapp.taiyo.anime.presentation.adapter.viewholder.OngoingsViewHolder
import com.taiyoapp.taiyo.anime.presentation.adapter.viewholder.ReleasedViewHolder
import com.taiyoapp.taiyo.anime.presentation.util.DateFormatter
import com.taiyoapp.taiyo.databinding.ItemAnonsBinding
import com.taiyoapp.taiyo.databinding.ItemOngoingsBinding
import com.taiyoapp.taiyo.databinding.ItemReleasedBinding

class SearchAdapter(
    private val context: Context,
) : PagingDataAdapter<Anime, ViewHolder>(AnimeDiffCallback) {
    var onAnimeClick: ((Anime) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = getViewBindingType(viewType, parent)
        return getViewHolderType(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val anime = getItem(position)
        anime?.let {
            when (anime.status) {
                STATUS_ONGOING -> {
                    with((holder as OngoingsViewHolder).binding) {
                        with(it) {
                            Glide.with(context)
                                .load(it.image)
                                .transition(DrawableTransitionOptions.withCrossFade())
                                .into(sivPoster)
                            tvTitle.text = it.title
                            tvEpisodesAired.text = it.episodesAired
                            tvEpisodesTotal.text = context.getString(
                                R.string.ongoings_episodes,
                                episodesTotal
                            )
                            root.setOnClickListener { onAnimeClick?.invoke(this) }
                        }
                    }
                }
                STATUS_ANONS -> {
                    with((holder as AnonsViewHolder).binding) {
                        with(it) {
                            Glide.with(context)
                                .load(it.image)
                                .transition(DrawableTransitionOptions.withCrossFade())
                                .into(sivPoster)
                            tvTitle.text = it.title
                            tvDate.text = DateFormatter.formatAiredOn(it.airedOn)
                            root.setOnClickListener { onAnimeClick?.invoke(this) }
                        }
                    }
                }
                STATUS_RELEASED -> {
                    with((holder as ReleasedViewHolder).binding) {
                        with(it) {
                            Glide.with(context)
                                .load(it.image)
                                .transition(DrawableTransitionOptions.withCrossFade())
                                .into(sivPoster)
                            tvTitle.text = it.title
                            tvEpisodesTotal.text =
                                context.getString(R.string.ongoings_episodes, episodesTotal)
                            root.setOnClickListener { onAnimeClick?.invoke(this) }
                        }
                    }
                }
                else -> throw RuntimeException("status is invalid")
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        val item = getItem(position)
        val viewType =  item?.let {
            when (it.status) {
                STATUS_ONGOING -> VIEW_TYPE_ONGOING
                STATUS_ANONS -> VIEW_TYPE_ANONS
                STATUS_RELEASED -> VIEW_TYPE_RELEASED
                else -> throw RuntimeException("status is invalid")
            }
        }
        return viewType ?: throw RuntimeException("item is null")
    }

    private fun getViewBindingType(viewType: Int, parent: ViewGroup): ViewBinding {
        val binding = when (viewType) {
            VIEW_TYPE_ONGOING -> ItemOngoingsBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
            VIEW_TYPE_ANONS -> ItemAnonsBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
            VIEW_TYPE_RELEASED -> ItemReleasedBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
            else -> throw RuntimeException("viewType is invalid")
        }
        return binding
    }

    private fun getViewHolderType(binding: ViewBinding): ViewHolder {
        val viewHolder = when (binding) {
            is ItemOngoingsBinding -> OngoingsViewHolder(binding)
            is ItemAnonsBinding -> AnonsViewHolder(binding)
            is ItemReleasedBinding -> ReleasedViewHolder(binding)
            else -> throw RuntimeException("binding is invalid")
        }
        return viewHolder
    }

    companion object {
        private const val VIEW_TYPE_ONGOING = 0
        private const val VIEW_TYPE_ANONS = 1
        private const val VIEW_TYPE_RELEASED = 2
        private const val STATUS_ONGOING = "ongoing"
        private const val STATUS_ANONS = "anons"
        private const val STATUS_RELEASED = "released"
    }
}