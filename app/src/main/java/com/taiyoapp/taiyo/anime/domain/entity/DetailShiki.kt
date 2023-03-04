package com.taiyoapp.taiyo.anime.domain.entity

data class DetailShiki(
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
    val ratesScoresStats: LinkedHashMap<String, Float>,
    val nextEpisodeAt: Long,
    val genres: List<String>,
    val studio: String,
) {
    data class RatesScoresStats(
        val name: String,
        val value: Float,
    )
}