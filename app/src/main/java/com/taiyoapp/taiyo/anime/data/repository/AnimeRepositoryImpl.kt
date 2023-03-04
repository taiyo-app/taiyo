package com.taiyoapp.taiyo.anime.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.taiyoapp.taiyo.anime.data.mapper.AnimeMapper
import com.taiyoapp.taiyo.anime.data.network.kodik.ApiFactoryKodik
import com.taiyoapp.taiyo.anime.data.network.myanimelist.ApiFactoryMal
import com.taiyoapp.taiyo.anime.data.network.shikimori.AnimePageLoader
import com.taiyoapp.taiyo.anime.data.network.shikimori.AnimePagingSource
import com.taiyoapp.taiyo.anime.data.network.shikimori.ApiFactoryShiki
import com.taiyoapp.taiyo.anime.domain.entity.*
import com.taiyoapp.taiyo.anime.domain.entity.Episodes.Result
import com.taiyoapp.taiyo.anime.domain.repository.AnimeRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class AnimeRepositoryImpl : AnimeRepository {
    private val apiServiceShiki = ApiFactoryShiki.apiServiceShiki
    private val apiServiceMal = ApiFactoryMal.apiServiceMAL
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

    override suspend fun getEpisodes(id: Int): List<Result> {
        return mapper.mapEpisodeListDtoToEntity(
            apiServiceKodik.getEpisodes(SEARCH_URL, id)
        ).results
    }

    override suspend fun getDetailMal(id: Int): Flow<DetailMal> = flow {
        val detailMAL = mapper.mapDetailMalDtoToEntity(
            apiServiceMal.getDetailMal(id)
        )
        emit(detailMAL)
    }

    override suspend fun getDetailShiki(id: Int): Flow<DetailShiki> = flow {
        val animeDetail = mapper.mapDetailShikiDtoToEntity(
            apiServiceShiki.getAnimeDetail(id)
        )
        emit(animeDetail)
    }

    override suspend fun getVideo(id: Int): Flow<List<Video>> = flow {
        val videoList = mapper.mapJsonArrayVideoDtoToEntity(
            apiServiceShiki.getVideo(id)
        ).filter { video -> video.hosting != "vk" }
        emit(videoList.toList())
    }

    override suspend fun getScreenshots(id: Int): Flow<List<Screenshot>> = flow {
        val screenshots = mapper.mapJsonArrayScreenshotsDtoToEntity(
            apiServiceShiki.getScreenshots(id)
        ).take(8)
        emit(screenshots.toList())
    }

    override suspend fun getSimilar(id: Int): Flow<List<Anime>> = flow {
        val similar = mapper.mapJsonArraySimilarDtoToEntity(
            apiServiceShiki.getSimilar(id)
        )
        emit(similar.toList())
    }

    companion object {
        private const val PAGE_SIZE = 50

        private const val SEARCH_URL = "https://kodikapi.com/search"
    }
}