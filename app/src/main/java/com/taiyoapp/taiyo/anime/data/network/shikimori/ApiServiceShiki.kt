package com.taiyoapp.taiyo.anime.data.network.shikimori

import com.taiyoapp.taiyo.anime.data.network.model.AnimeDto
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.QueryMap

interface ApiServiceShiki {
    @GET("animes")
    suspend fun getAnimeList(
        @Header(HEADER_PARAM_USER_AGENT) appName: String = APPLICATION_NAME,
        @QueryMap queryMap: HashMap<String, Any>
    ): List<AnimeDto>

    companion object {
        // requirement
        private const val HEADER_PARAM_USER_AGENT = "User-Agent"
        private const val APPLICATION_NAME = "Taiyo"
    }
}