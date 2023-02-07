package com.taiyoapp.taiyo.anime.data.network.kodik

import com.taiyoapp.taiyo.anime.data.network.model.EpisodeListDto
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Url

interface ApiServiceKodik {
    @GET
    suspend fun getEpisodesList(
        @Url url: String,
        @Query("shikimori_id") shikimoriId: Int,
        @Query("token") token: String = "b7cc4293ed475c4ad1fd599d114f4435",
        @Query("with_episodes_data") withEpisodesData: Boolean = true,
        @Query("prioritize_translations") prioritizeTranslations: String =
            "610,923,557,643,609,1895,767,1978,1863,1002,subtitles",
    ): EpisodeListDto
}