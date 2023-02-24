package com.taiyoapp.taiyo.anime.presentation.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.taiyoapp.taiyo.anime.domain.entity.Anime
import com.taiyoapp.taiyo.anime.presentation.adapter.viewholder.AnonsViewHolder
import com.taiyoapp.taiyo.anime.presentation.util.DateFormatter
import com.taiyoapp.taiyo.databinding.ItemAnonsBinding

class AnonsAdapter(
    private val context: Context,
) : PagingDataAdapter<Anime, ViewHolder>(AnimeDiffCallback) {
    var onAnimeClick: ((Anime) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemAnonsBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return AnonsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val anime = getItem(position)
        anime?.let {
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
    }
}