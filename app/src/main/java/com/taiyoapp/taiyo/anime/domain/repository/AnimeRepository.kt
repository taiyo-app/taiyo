package com.taiyoapp.taiyo.anime.domain.repository

import androidx.paging.PagingData
import com.taiyoapp.taiyo.anime.domain.entity.Anime
import kotlinx.coroutines.flow.Flow

interface AnimeRepository {
    suspend fun getAnimeList(queryMap: HashMap<String, Any>): Flow<PagingData<Anime>>
}