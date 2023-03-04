package com.taiyoapp.taiyo.anime.data.network.myanimelist

import com.taiyoapp.taiyo.anime.data.network.model.DetailMalDto
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiServiceMal {
    @GET("anime/{anime_id}")
    suspend fun getDetailMal(
        @Path(QUERY_PARAM_ID) anime_id: Int,
        @Header(HEADER_PARAM_X_MAL_CLIENT_ID) clientId: String = CLIENT_ID,
        @Query("fields") fields: String = "main_picture,mean,num_scoring_users,statistics",
    ): DetailMalDto

    companion object {
        private const val HEADER_PARAM_X_MAL_CLIENT_ID = "X-MAL-CLIENT-ID"
        private const val CLIENT_ID = "d6d5d76f3e4968435091e113f2aa432d"

        private const val QUERY_PARAM_ID = "anime_id"
    }
}