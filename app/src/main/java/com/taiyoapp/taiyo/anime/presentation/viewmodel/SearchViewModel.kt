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
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
class SearchViewModel : ViewModel() {
    private val repository = AnimeRepositoryImpl()

    private val getAnimeListUseCase = GetAnimeListUseCase(repository)

    lateinit var animeFlow: Flow<PagingData<Anime>>

    private val queryMap = QueryMapHelper.getSearchQueryMap()

    private val searchBy = MutableLiveData(BASE_SEARCH_VALUE)

    init {
        viewModelScope.launch {
            animeFlow = searchBy.asFlow()
                .debounce(100)
                .flatMapLatest {
                    queryMap[QueryMapHelper.SEARCH] = searchBy.value.toString()
                    getAnimeListUseCase(queryMap)
                }
                .cachedIn(viewModelScope)
        }
    }

    fun setSearchBy(value: String) {
        if (this.searchBy.value == value) return
        this.searchBy.value = value
    }

    companion object {
        const val BASE_SEARCH_VALUE = "@"
    }
}