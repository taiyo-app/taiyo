package com.taiyoapp.taiyo.anime.domain.entity

data class AnimeDetail(
    val id: Int,
    val name: String?,
    val russian: String?,
    val kind: String?,
    val status: String?,
    val episodesAired: String?,
    val episodesTotal: String?,
    val releasedOn: String?,
    val duration: Int?,
    val description: String?,
    val nextEpisodeAt: String?,
    val genres: List<Genre>?,
    val studios: List<Studio>?
) {
    data class Genre(
        val russian: String?
    )
    data class Studio(
        val name: String
    )
}