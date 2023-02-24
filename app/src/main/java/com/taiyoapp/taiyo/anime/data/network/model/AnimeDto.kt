package com.taiyoapp.taiyo.anime.data.network.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class AnimeDto(
    @SerializedName("id")
    @Expose
    val id: Int,
    @SerializedName("russian")
    @Expose
    val russian: String?,
    @SerializedName("image")
    @Expose
    val animeImageDto: AnimeImageDto?,
    @SerializedName("status")
    @Expose
    val status: String?,
    @SerializedName("episodes")
    @Expose
    val episodes: String?,
    @SerializedName("episodes_aired")
    @Expose
    val episodesAired: String?,
    @SerializedName("aired_on")
    @Expose
    val airedOn: String?,
) {
    data class AnimeImageDto(
        @SerializedName("original")
        @Expose
        val original: String
    )
}