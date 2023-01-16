package com.example.taiyo.presentation.utils

object QueryMapHelper {
    fun getOngoingsQueryMap() = HashMap<String, Any>().apply {
        this["order"] = "popularity"
        this["kind"] = "tv"
        this["status"] = "ongoing"
        this["season"] = "winter_2023"
        this["score"] = 1
        this["duration"] = "!S"
    }
}