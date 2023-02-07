package com.taiyoapp.taiyo.anime.data.network.kodik

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiFactoryKodik {

    private const val BASE_URL = "http://localhost/"

    private val retrofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(BASE_URL)
        .build()

    val apiServiceKodik: ApiServiceKodik = retrofit.create(ApiServiceKodik::class.java)

}