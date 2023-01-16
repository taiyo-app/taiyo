package com.example.taiyo.domain.repository

import androidx.paging.PagingData
import com.example.taiyo.domain.entity.Anime
import kotlinx.coroutines.flow.Flow

interface AnimeRepository {
    suspend fun getAnimeList(queryMap: HashMap<String, Any>): Flow<PagingData<Anime>>
}