package com.taiyoapp.taiyo.anime.data.network.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class EpisodesDto(
    @SerializedName("results")
    @Expose
    val results: List<ResultDto>?,
) {
    data class ResultDto(
        @SerializedName("link")
        @Expose
        val link: String?,
        @SerializedName("translation")
        @Expose
        val translation: TranslationDto?,
        @SerializedName("screenshots")
        @Expose
        val screenshots: List<String>?,
        @SerializedName("episodes_count")
        @Expose
        val episodesCount: Int?,
        @SerializedName("seasons")
        @Expose
        val seasons: Map<Int, SeasonDto>?,
        @SerializedName("last_season")
        @Expose
        val lastSeason: Int?,
    )

    data class TranslationDto(
        @SerializedName("id")
        @Expose
        val id: Int?,
        @SerializedName("title")
        @Expose
        val title: String?,
        @SerializedName("type")
        @Expose
        val type: String?,
    )

    data class SeasonDto(
        @SerializedName("episodes")
        @Expose
        val episodes: Map<Int, EpisodeDto>?,
    )

    data class EpisodeDto(
        @SerializedName("link")
        @Expose
        val link: String?,
        @SerializedName("screenshots")
        @Expose
        val screenshots: List<String>?,
    )
}

