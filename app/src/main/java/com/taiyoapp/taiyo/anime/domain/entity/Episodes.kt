package com.taiyoapp.taiyo.anime.domain.entity

data class Episodes(
    val results: List<Result>,
) {
    data class Result(
        val translation: Translation,
        val episodesCount: Int,
        val seasons: List<Season>,
    ) {
        override fun toString(): String {
            return episodesCount.toString()
        }
    }

    data class Translation(
        val id: Int,
        val title: String,
        val type: String,
    ) {
        override fun toString(): String {
            return title
        }
    }

    data class Season(
        val episodes: List<Episode>,
    )

    data class Episode(
        val link: String,
        val screenshots: List<String>,
    )
}

