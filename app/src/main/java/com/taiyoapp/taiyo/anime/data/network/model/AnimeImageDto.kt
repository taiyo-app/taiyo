package com.taiyoapp.taiyo.anime.data.network.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class AnimeImageDto(
    @SerializedName("original")
    @Expose
    val original: String?
)