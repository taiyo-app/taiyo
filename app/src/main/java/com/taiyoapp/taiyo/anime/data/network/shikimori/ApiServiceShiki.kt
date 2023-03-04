package com.taiyoapp.taiyo.anime.data.network.shikimori

import com.google.gson.JsonElement
import com.taiyoapp.taiyo.anime.data.network.model.AnimeDto
import com.taiyoapp.taiyo.anime.data.network.model.DetailShikiDto
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.QueryMap

interface ApiServiceShiki {
    @GET("animes")
    suspend fun getAnimeList(
        @Header(HEADER_PARAM_USER_AGENT) appName: String = APPLICATION_NAME,
        @QueryMap queryMap: HashMap<String, Any>,
    ): List<AnimeDto>

    @GET("animes/{id}")
    suspend fun getAnimeDetail(
        @Path(QUERY_PARAM_ID) id: Int,
    ): DetailShikiDto

    @GET("animes/{id}/videos")
    suspend fun getVideo(
        @Path(QUERY_PARAM_ID) id: Int,
    ): JsonElement

    @GET("animes/{id}/screenshots")
    suspend fun getScreenshots(
        @Path(QUERY_PARAM_ID) id: Int,
    ): JsonElement

    @GET("animes/{id}/similar")
    suspend fun getSimilar(
        @Path(QUERY_PARAM_ID) id: Int,
    ): JsonElement

    companion object {
        // requirement
        private const val HEADER_PARAM_USER_AGENT = "User-Agent"
        private const val APPLICATION_NAME = "Taiyo"

        private const val QUERY_PARAM_ID = "id"
    }
}