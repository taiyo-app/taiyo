package com.taiyoapp.taiyo.anime.domain.entity

data class Anime(
    val id: Int,
    val title: String?,
    val image: String?,
    val status: String?,
    val episodesAired: String?,
    val episodesTotal: String?,
)