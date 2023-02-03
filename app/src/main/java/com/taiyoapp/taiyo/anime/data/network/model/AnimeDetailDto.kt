package com.taiyoapp.taiyo.anime.data.network.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class AnimeDetailDto(
    @SerializedName("id")
    @Expose
    val id: Int,
    @SerializedName("name")
    @Expose
    val name: String,
    @SerializedName("russian")
    @Expose
    val russian: String,
    @SerializedName("kind")
    @Expose
    val kind: String,
    @SerializedName("status")
    @Expose
    val status: String,
    @SerializedName("episodes")
    @Expose
    val episodes: String,
    @SerializedName("episodes_aired")
    @Expose
    val episodesAired: String,
    @SerializedName("released_on")
    @Expose
    val releasedOn: String,
    @SerializedName("duration")
    @Expose
    val duration: Int,
    @SerializedName("description")
    @Expose
    val description: String?,
    @SerializedName("next_episode_at")
    @Expose
    val nextEpisodeAt: String?,
    @SerializedName("genres")
    @Expose
    val genres: List<GenreDto>?,
    @SerializedName("studios")
    @Expose
    val studios: List<StudioDto>?
) {
    data class GenreDto(
        @SerializedName("russian")
        @Expose
        val russian: String
    )
    data class StudioDto(
        @SerializedName("name")
        @Expose
        val name: String
    )
}