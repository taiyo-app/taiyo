package com.taiyoapp.taiyo.anime.data.network.shikimori

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

// shikimori api allows to get lists and detailed information of anime
object ApiFactoryShiki {
    private const val BASE_URL = "https://shikimori.one/api/"
    private val retrofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(BASE_URL)
        .build()
    val apiServiceShiki: ApiServiceShiki = retrofit.create(ApiServiceShiki::class.java)
}