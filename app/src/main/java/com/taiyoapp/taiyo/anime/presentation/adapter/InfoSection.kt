package com.taiyoapp.taiyo.anime.presentation.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.taiyoapp.taiyo.R
import com.taiyoapp.taiyo.anime.presentation.adapter.viewholder.InfoViewHolder
import io.github.luizgrp.sectionedrecyclerviewadapter.Section
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionParameters


internal class InfoSection(
    private val infoLabel: String,
    private val infoValue: String
) : Section(SectionParameters.builder()
    .itemResourceId(R.layout.item_info)
    .build()) {

    override fun getContentItemsTotal(): Int {
        return 1
    }

    override fun getItemViewHolder(view: View): RecyclerView.ViewHolder {
        return InfoViewHolder(view)
    }

    override fun onBindItemViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val infoViewHolder: InfoViewHolder = holder as InfoViewHolder
        with (infoViewHolder) {
            tvInfoLabel.text = infoLabel
            tvInfoValue.text = infoValue
        }
    }
}