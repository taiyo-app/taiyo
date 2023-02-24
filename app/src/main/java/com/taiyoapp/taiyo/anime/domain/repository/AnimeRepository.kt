package com.taiyoapp.taiyo.anime.domain.repository

import androidx.paging.PagingData
import com.taiyoapp.taiyo.anime.domain.entity.Anime
import com.taiyoapp.taiyo.anime.domain.entity.AnimeDetail
import com.taiyoapp.taiyo.anime.domain.entity.EpisodeList
import kotlinx.coroutines.flow.Flow

interface AnimeRepository {
    suspend fun getAnimeList(queryMap: HashMap<String, Any>): Flow<PagingData<Anime>>

    suspend fun getAnimeDetail(id: Int): Flow<AnimeDetail>

    suspend fun getPoster(id: Int): Flow<String>

    suspend fun getEpisodeList(id: Int): List<EpisodeList.Result>
}