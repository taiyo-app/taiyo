package com.taiyoapp.taiyo.anime.presentation.util

object QueryMapHelper {
    fun getOngoingsQueryMap() = HashMap<String, Any>().apply {
        this[ORDER] = VALUE_ORDER[0]
        this[KIND] = VALUE_KIND[0]
        this[STATUS] = VALUE_STATUS[0]
        this[SEASON] = "${VALUE_SEASON[0]},${VALUE_SEASON[1]}"
        this[SCORE] = VALUE_SCORE
        this[DURATION] = VALUE_DURATION
        // NO HORNY
        this[CENSORED] = VALUE_CENSORED
    }
    fun getAnonsQueryMap() = HashMap<String, Any>().apply {
        this[ORDER] = VALUE_ORDER[0]
        this[KIND] = VALUE_KIND[0]
        this[STATUS] = VALUE_STATUS[1]
        this[SEASON] = "${VALUE_SEASON[1]},${VALUE_SEASON[2]}"
        this[CENSORED] = VALUE_CENSORED
    }
    fun getReleasedQueryMap() = HashMap<String, Any>().apply {
        this[ORDER] = VALUE_ORDER[1]
        this[KIND] = VALUE_KIND[0]
        this[STATUS] = VALUE_STATUS[2]
        this[SCORE] = VALUE_SCORE
        this[DURATION] = VALUE_DURATION
        this[CENSORED] = VALUE_CENSORED
    }
    fun getMoviesQueryMap() = HashMap<String, Any>().apply {
        this[ORDER] = VALUE_ORDER[0]
        this[KIND] = VALUE_KIND[1]
        this[CENSORED] = VALUE_CENSORED
    }
    fun getSearchQueryMap() = HashMap<String, Any>().apply {
        this[ORDER] = VALUE_ORDER[0]
        this[SEARCH] = VALUE_SEARCH
        this[CENSORED] = VALUE_CENSORED
    }

    private const val ORDER = "order"
    private const val KIND = "kind"
    private const val STATUS = "status"
    private const val SEASON = "season"
    private const val SCORE = "score"
    private const val DURATION = "duration"
    private const val CENSORED = "censored"
    const val SEARCH = "search"

    private val VALUE_ORDER = listOf(
        "popularity",
        "aired_on",
    )
    private val VALUE_KIND = listOf(
        "tv",
        "movie"
    )
    private val VALUE_STATUS = listOf(
        "ongoing",
        "anons",
        "released"
    )
    // TODO: добавить вычисление сезона для автоматизации
    private val VALUE_SEASON = listOf(
        "fall_2022",
        "winter_2023",
        "spring_2023"
    )
    private const val VALUE_SCORE = 1
    private const val VALUE_DURATION = "!S"
    private const val VALUE_CENSORED = "true"
    private const val VALUE_SEARCH = "search"
}