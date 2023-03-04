package com.taiyoapp.taiyo.anime.domain.repository

import androidx.paging.PagingData
import com.taiyoapp.taiyo.anime.domain.entity.*
import kotlinx.coroutines.flow.Flow

interface AnimeRepository {
    suspend fun getAnimeList(queryMap: HashMap<String, Any>): Flow<PagingData<Anime>>

    suspend fun getDetailShiki(id: Int): Flow<DetailShiki>

    suspend fun getDetailMal(id: Int): Flow<DetailMal>

    suspend fun getVideo(id: Int): Flow<List<Video>>

    suspend fun getScreenshots(id: Int): Flow<List<Screenshot>>

    suspend fun getSimilar(id: Int): Flow<List<Anime>>

    suspend fun getEpisodes(id: Int): List<Episodes.Result>
}