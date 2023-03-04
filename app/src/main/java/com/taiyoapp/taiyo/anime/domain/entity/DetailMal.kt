package com.taiyoapp.taiyo.anime.domain.entity

data class DetailMal(
    val mainPicture: MainPicture,
    val numScoringUsers: Int,
    val statistics: Statistics,
) {
    data class MainPicture(
        val large: String,
    )

    data class Statistics(
        val status: Status,
        val numListUsers: Int,
    ) {
        data class Status(
            val watching: String,
            val completed: String,
            val onHold: String,
            val dropped: String,
            val planToWatch: String,
        )
    }
}