package com.taiyoapp.taiyo.anime.domain.entity

data class AnimeDetail(
    val id: Int,
    val name: String,
    val russian: String,
    val kind: String,
    val score: String,
    val status: String,
    val episodesAired: String,
    val episodesTotal: String,
    val airedOn: String,
    val duration: String,
    val description: String,
    val nextEpisodeAt: Long,
    val genres: List<String>,
    val studio: String
)