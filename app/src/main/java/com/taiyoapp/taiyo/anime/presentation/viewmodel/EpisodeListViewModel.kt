package com.taiyoapp.taiyo.anime.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.taiyoapp.taiyo.anime.data.repository.AnimeRepositoryImpl
import com.taiyoapp.taiyo.anime.domain.entity.EpisodeList.*
import com.taiyoapp.taiyo.anime.domain.usecase.GetEpisodeListUseCase
import kotlinx.coroutines.launch

class EpisodeListViewModel : ViewModel() {
    private val repository = AnimeRepositoryImpl()

    private val getEpisodeListUseCase = GetEpisodeListUseCase(repository)

    private val _episodeList = MutableLiveData<List<Result>>()

    private val _translations = MutableLiveData<List<Translation>>()
    val translations: LiveData<List<Translation>>
        get() = _translations

    private val _episodes = MutableLiveData<List<Episode>>()
    val episodes: LiveData<List<Episode>>
        get() = _episodes

    fun loadTranslations(animeId: Int) {
        viewModelScope.launch {
            _episodeList.value = getEpisodeListUseCase(animeId)!!
            val translationList = arrayListOf<Translation>()
            for (result in _episodeList.value!!) {
                translationList.add(result.translation)
            }
            _translations.value = translationList
        }
    }

    fun loadEpisodes(translationId: Int) {
        for (result in _episodeList.value!!) {
            if (translationId == result.translation.id) {
                if (result.seasons.size > 1) {
                    // TODO() убрать сезон со спешлами
                    throw RuntimeException(
                        "В списке 2 сезона одного сезона (спешлы сезона/эпизоды сезона)"
                    )
                } else {
                    _episodes.value = result.seasons[0].episodes
                }
            }
        }
    }
}