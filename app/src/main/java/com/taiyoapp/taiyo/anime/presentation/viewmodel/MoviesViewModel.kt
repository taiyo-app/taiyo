package com.taiyoapp.taiyo.anime.presentation.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.taiyoapp.taiyo.anime.data.repository.AnimeRepositoryImpl
import com.taiyoapp.taiyo.anime.domain.entity.Anime
import com.taiyoapp.taiyo.anime.domain.usecase.GetAnimeListUseCase
import com.taiyoapp.taiyo.anime.presentation.util.QueryMapHelper
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
class MoviesViewModel : ViewModel() {
    private val repository = AnimeRepositoryImpl()

    private val getAnimeListUseCase = GetAnimeListUseCase(repository)

    lateinit var animeFlow: Flow<PagingData<Anime>>

    private val queryMap = QueryMapHelper.getMoviesQueryMap()

    private var shouldRefresh = MutableLiveData(Unit)

    init {
        viewModelScope.launch {
            animeFlow = shouldRefresh.asFlow()
                .flatMapLatest { getAnimeListUseCase(queryMap) }
                .cachedIn(viewModelScope)
        }
    }

    fun refresh() {
        shouldRefresh.value = Unit
    }
}