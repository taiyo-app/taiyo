package com.taiyoapp.taiyo.anime.data.network.anilist

import com.apollographql.apollo3.ApolloClient

object ApolloClientAni {
    private const val BASE_URL = "https://graphql.anilist.co/"

    val apolloClientAni = ApolloClient.Builder()
        .serverUrl(BASE_URL)
        .build()
}