package com.taiyoapp.taiyo.anime.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.apollographql.apollo3.api.Optional
import com.taiyoapp.taiyo.MediaQuery
import com.taiyoapp.taiyo.anime.data.mapper.AnimeMapper
import com.taiyoapp.taiyo.anime.data.network.anilist.ApolloClientAni
import com.taiyoapp.taiyo.anime.data.network.kodik.ApiFactoryKodik
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
    private val apolloClientAni = ApolloClientAni.apolloClientAni
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

    override suspend fun getAnimeDetail(id: Int): Flow<AnimeDetail> {
        return flow {
            val animeDetail = mapper.mapAnimeDetailDtoToEntity(
                apiServiceShiki.getAnimeDetail(id)
            )
            emit(animeDetail)
        }
    }

    override suspend fun getAnimeMedia(id: Int): MediaQuery.Data {
        val animeMedia = try {
            apolloClientAni.query(
                MediaQuery(
                    Optional.presentIfNotNull(id)
                )
            )
                .execute()
                .data
        } catch (e: Exception) {
            throw RuntimeException(e.message)
        }
        return animeMedia ?: MediaQuery.Data(null)
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