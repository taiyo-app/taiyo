package com.example.taiyo.presentation.anime.page.anons

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.taiyo.data.repository.AnimeRepositoryImpl
import com.example.taiyo.domain.entity.Anime
import com.example.taiyo.domain.usecases.GetAnimeListUseCase
import com.example.taiyo.presentation.anime.page.utils.QueryMapHelper
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
class AnonsViewModel : ViewModel() {
    private val repository = AnimeRepositoryImpl()

    private val getAnimeListUseCase = GetAnimeListUseCase(repository)

    lateinit var animeFlow: Flow<PagingData<Anime>>

    private val queryMap = QueryMapHelper.getAnonsQueryMap()

    private var shouldRefresh = MutableLiveData(Unit)

    init {
        viewModelScope.launch {
            animeFlow = shouldRefresh.asFlow()
                .flatMapLatest { getAnimeListUseCase(queryMap) }
                // always use cachedIn operator for flows returned by Pager. Otherwise exception may be thrown
                // when 1) refreshing/invalidating or 2) subscribing to the flow more than once
                .cachedIn(viewModelScope)
        }
    }

    fun refresh() {
        shouldRefresh.value = Unit
    }
}