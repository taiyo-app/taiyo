package com.taiyoapp.taiyo.anime.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.taiyoapp.taiyo.anime.data.mapper.AnimeMapper
import com.taiyoapp.taiyo.anime.data.network.kodik.ApiFactoryKodik
import com.taiyoapp.taiyo.anime.data.network.myanimelist.ApiFactoryMAL
import com.taiyoapp.taiyo.anime.data.network.shikimori.AnimePageLoader
import com.taiyoapp.taiyo.anime.data.network.shikimori.AnimePagingSource
import com.taiyoapp.taiyo.anime.data.network.shikimori.ApiFactoryShiki
import com.taiyoapp.taiyo.anime.domain.entity.Anime
import com.taiyoapp.taiyo.anime.domain.entity.AnimeDetail
import com.taiyoapp.taiyo.anime.domain.entity.EpisodeList.Result
import com.taiyoapp.taiyo.anime.domain.repository.AnimeRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class AnimeRepositoryImpl : AnimeRepository {
    private val apiServiceShiki = ApiFactoryShiki.apiServiceShiki
    private val apiServiceMAL = ApiFactoryMAL.apiServiceMAL
    private val apiServiceKodik = ApiFactoryKodik.apiServiceKodik
    private val mapper = AnimeMapper()

    // with using paging
    override suspend fun getAnimeList(queryMap: HashMap<String, Any>): Flow<PagingData<Anime>> {
        val loader: AnimePageLoader = { pageIndex, pageSize ->
            queryMap["page"] = pageIndex
            queryMap["limit"] = pageSize
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
            pagingSourceFactory = {
                AnimePagingSource(loader)
            }
        ).flow
    }

    override suspend fun getPoster(id: Int): Flow<String> = flow {
        val poster = mapper.mapPosterDtoToEntity(
            apiServiceMAL.getPoster(id)
        )
        emit(poster)
    }

    override suspend fun getAnimeDetail(id: Int): Flow<AnimeDetail> = flow {
        val animeDetail = mapper.mapAnimeDetailDtoToEntity(
            apiServiceShiki.getAnimeDetail(id)
        )
        emit(animeDetail)
    }

    override suspend fun getEpisodeList(id: Int): List<Result> {
        return mapper.mapEpisodeListDtoToEntity(
            apiServiceKodik.getEpisodesList(SEARCH_URL, id)
        ).results
    }

    companion object {
        private const val PAGE_SIZE = 50

        private const val SEARCH_URL = "https://kodikapi.com/search"
    }
}