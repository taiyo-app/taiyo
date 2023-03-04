package com.taiyoapp.taiyo.anime.data.network.myanimelist

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiFactoryMal {

    private const val BASE_URL = "https://api.myanimelist.net/v2/"

    private val retrofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(BASE_URL)
        .build()

    val apiServiceMAL: ApiServiceMal = retrofit.create(ApiServiceMal::class.java)

}