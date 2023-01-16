package com.example.taiyo.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.taiyo.data.mapper.AnimeMapper
import com.example.taiyo.data.network.shikimori.AnimePageLoader
import com.example.taiyo.data.network.shikimori.AnimePagingSource
import com.example.taiyo.data.network.shikimori.ApiFactoryShiki
import com.example.taiyo.domain.entity.Anime
import com.example.taiyo.domain.repository.AnimeRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow

class AnimeRepositoryImpl : AnimeRepository {
    private val apiServiceShiki = ApiFactoryShiki.apiServiceShiki
    private val mapper = AnimeMapper()

    // with using paging
    override suspend fun getAnimeList(queryMap: HashMap<String, Any>): Flow<PagingData<Anime>> {
        val loader: AnimePageLoader = { pageIndex, pageSize ->
            queryMap["page"] = pageIndex
            queryMap["limit"] = pageSize
            delay(500)
            apiServiceShiki.getAnimeList(queryMap = queryMap)
                .map { mapper.mapAnimeDtoToEntity(it) }
        }
        return Pager(
            config = PagingConfig(
                pageSize = PAGE_SIZE,
                initialLoadSize = PAGE_SIZE,
                prefetchDistance = PAGE_SIZE / 2,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { AnimePagingSource(loader) }
        ).flow
    }

    companion object {
        private const val PAGE_SIZE = 5
    }
}