package com.taiyoapp.taiyo.anime.data.network.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class EpisodeListDto(
    @SerializedName("results")
    @Expose
    val results: List<ResultDto>
) {
    data class ResultDto(
        @SerializedName("translation")
        @Expose
        val translation: TranslationDto,
        @SerializedName("seasons")
        @Expose
        val seasons: Map<Int, SeasonDto>
    )
    data class TranslationDto(
        @SerializedName("id")
        @Expose
        val  id: Int,
        @SerializedName("title")
        @Expose
        val title: String,
        @SerializedName("type")
        @Expose
        val type: String
    )
    data class SeasonDto(
        @SerializedName("episodes")
        @Expose
        val episodes: Map<Int, EpisodeDto>
    )
    data class EpisodeDto(
        @SerializedName("link")
        @Expose
        val link: String,
        @SerializedName("screenshots")
        @Expose
        val screenshots: List<String>
    )
}

