package com.taiyoapp.taiyo.anime.presentation.adapter.viewholder

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.taiyoapp.taiyo.R

class InfoViewHolder(
    val view: View,
) : RecyclerView.ViewHolder(view) {
    val tvInfoLabel: TextView = view.findViewById(R.id.tv_info_label)
    val tvInfoValue: TextView = view.findViewById(R.id.tv_info_value)
}