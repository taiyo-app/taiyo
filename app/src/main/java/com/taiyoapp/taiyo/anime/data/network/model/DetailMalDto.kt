package com.taiyoapp.taiyo.anime.data.network.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class DetailMalDto(
    @SerializedName("main_picture")
    @Expose
    val mainPicture: MainPicture?,
    @SerializedName("num_scoring_users")
    @Expose
    val numScoringUsers: Int?,
    @SerializedName("statistics")
    @Expose
    val statistics: Statistics?,
) {
    data class MainPicture(
        @SerializedName("large")
        @Expose
        val large: String?,
    )

    data class Statistics(
        @SerializedName("status")
        @Expose
        val status: Status?,
        @SerializedName("num_list_users")
        @Expose
        val numListUsers: Int?,
    ) {
        data class Status(
            @SerializedName("watching")
            @Expose
            val watching: String?,
            @SerializedName("completed")
            @Expose
            val completed: String?,
            @SerializedName("on_hold")
            @Expose
            val onHold: String?,
            @SerializedName("dropped")
            @Expose
            val dropped: String?,
            @SerializedName("plan_to_watch")
            @Expose
            val planToWatch: String?,
        )
    }
}