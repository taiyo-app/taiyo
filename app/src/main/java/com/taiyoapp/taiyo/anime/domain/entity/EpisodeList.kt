package com.taiyoapp.taiyo.anime.domain.entity

data class EpisodeList(
    val results: List<Result>
) {
    data class Result(
        val translation: Translation,
        val seasons: List<Season>
    )
    data class Translation(
        val id: Int,
        val title: String,
        val type: String
    ) {
        override fun toString(): String {
            return title
        }
    }
    data class Season(
        val episodes: List<Episode>
    )
    data class Episode(
        val link: String,
        val screenshots: List<String>
    )
}

