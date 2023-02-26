package com.taiyoapp.taiyo.anime.data.network.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class VideoDto(
    @SerializedName("id")
    @Expose
    val id: Int,
    @SerializedName("url")
    @Expose
    val url: String?,
    @SerializedName("image_url")
    @Expose
    val imageUrl: String?,
    @SerializedName("player_url")
    @Expose
    val playerUrl: String?,
    @SerializedName("name")
    @Expose
    val name: String?,
    @SerializedName("kind")
    @Expose
    val kind: String?,
    @SerializedName("hosting")
    @Expose
    val hosting: String?
)