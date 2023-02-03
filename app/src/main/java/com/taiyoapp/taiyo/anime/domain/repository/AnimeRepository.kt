package com.taiyoapp.taiyo.anime.domain.repository

import androidx.paging.PagingData
import com.taiyoapp.taiyo.MediaQuery
import com.taiyoapp.taiyo.anime.domain.entity.Anime
import com.taiyoapp.taiyo.anime.domain.entity.AnimeDetail
import kotlinx.coroutines.flow.Flow

interface AnimeRepository {
    suspend fun getAnimeList(queryMap: HashMap<String, Any>): Flow<PagingData<Anime>>

    suspend fun getAnimeDetail(id: Int): AnimeDetail

    suspend fun getAnimeMedia(id: Int): MediaQuery.Data
}