package com.taiyoapp.taiyo.anime.domain.entity

data class Video(
    val id: Int,
    val url: String,
    val imageUrl: String,
    val playerUrl: String,
    val name: String,
    val kind: String,
    val hosting: String
)