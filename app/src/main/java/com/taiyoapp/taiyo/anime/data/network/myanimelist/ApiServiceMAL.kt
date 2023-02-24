package com.taiyoapp.taiyo.anime.data.network.myanimelist

import com.taiyoapp.taiyo.anime.data.network.model.DetailMALDto
import retrofit2.http.*

interface ApiServiceMAL {
    @GET("anime/{anime_id}")
    suspend fun getPoster(
        @Path(QUERY_PARAM_ID) anime_id: Int,
        @Header(HEADER_PARAM_X_MAL_CLIENT_ID) clientId: String = CLIENT_ID,
        @Query("fields") fields: String = "main_picture"
    ): DetailMALDto

    companion object {
        private const val HEADER_PARAM_X_MAL_CLIENT_ID = "X-MAL-CLIENT-ID"
        private const val CLIENT_ID = "d6d5d76f3e4968435091e113f2aa432d"

        private const val QUERY_PARAM_ID = "anime_id"
    }
}