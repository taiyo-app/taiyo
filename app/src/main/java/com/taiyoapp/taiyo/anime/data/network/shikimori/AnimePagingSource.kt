package com.taiyoapp.taiyo.anime.data.network.shikimori

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.taiyoapp.taiyo.anime.domain.entity.Anime

typealias AnimePageLoader = suspend (pageIndex: Int, pageSize: Int) -> List<Anime>

class AnimePagingSource(
    private val loader: AnimePageLoader
) : PagingSource<Int, Anime>() {
    // load data with paging params
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Anime> {
        // get the index of page to be loaded
        val pageIndex = params.key ?: 1
        return try {
            // load the anime page with pageIndex
            val animeList = loader.invoke(pageIndex, params.loadSize)
            // if success return LoadResult.Page
            return LoadResult.Page(
                // loaded data
                data = animeList,
                // index of the previous page if exists
                prevKey = if (pageIndex == 1) null else pageIndex - 1,
                // index of the next page if exists;
                // by default the first load may be larger (x3 times)
                nextKey = if (animeList.size == params.loadSize) pageIndex + 1 else null
            )
        } catch (e: Exception) {
            // if failed return LoadResult.Error
            LoadResult.Error(
                throwable = e
            )
        }
    }

    // update data, which returns pageIndex (key)
    override fun getRefreshKey(state: PagingState<Int, Anime>): Int? {
        // get the most recently index in the anime list
        val anchorPosition = state.anchorPosition ?: return null
        // convert item index to page index
        val page = state.closestPageToPosition(anchorPosition) ?: return null
        // page doesn't have 'currentKey' property, so need to calculate i manually
        return page.prevKey?.plus(1) ?: page.nextKey?.minus(1)
    }
}