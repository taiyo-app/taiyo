package com.taiyoapp.taiyo.anime.domain.entity

data class Episodes(
    val results: List<Result>
) {
    data class Result(
        val link: String,
        val translation: Translation,
        val screenshots: List<String>,
        val episodesCount: Int,
        val lastSeason: Int,
        val seasons: Map<Int, Season>,
    )

    data class Translation(
        val id: Int,
        val title: String,
        val type: String,
        val episodesCount: Int
    ) {
        override fun toString(): String {
            return if (episodesCount == 0) {
                title
            } else {
                "$title  •  $episodesCount эп"
            }
        }
    }

    data class Season(
        val episodes: List<Episode>
    )

    data class Episode(
        val link: String,
        val screenshots: List<String>,
        val episodeNumber: Int
    )
}

