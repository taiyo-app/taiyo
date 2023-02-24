package com.taiyoapp.taiyo.anime.data.network.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class DetailMALDto(
    @SerializedName("main_picture")
    @Expose
    val mainPicture: MainPicture
) {
    data class MainPicture(
        @SerializedName("large")
        @Expose
        val large: String?
    )
}