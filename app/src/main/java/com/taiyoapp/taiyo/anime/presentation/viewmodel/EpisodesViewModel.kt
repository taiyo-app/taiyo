package com.taiyoapp.taiyo.anime.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.taiyoapp.taiyo.anime.data.repository.AnimeRepositoryImpl
import com.taiyoapp.taiyo.anime.domain.entity.Episodes
import com.taiyoapp.taiyo.anime.domain.entity.Episodes.*
import com.taiyoapp.taiyo.anime.domain.usecase.GetEpisodesUseCase
import kotlinx.coroutines.launch

class EpisodesViewModel : ViewModel() {
    private val repository = AnimeRepositoryImpl()

    private val getEpisodesUseCase = GetEpisodesUseCase(repository)

    private val _episodeList = MutableLiveData<List<Result>>()

    private val _translations = MutableLiveData<List<Translation>>()
    val translations: LiveData<List<Translation>>
        get() = _translations

    private val _episodes = MutableLiveData<List<Episode>>()
    val episodes: LiveData<List<Episode>>
        get() = _episodes

    fun loadTranslations(animeId: Int) {
        viewModelScope.launch {
            val episodes = getEpisodesUseCase(animeId)
            if (episodes.isNotEmpty()) {
                _episodeList.value = getEpisodesUseCase(animeId)!!
                val translationList = arrayListOf<Translation>()
                for (result in _episodeList.value!!) {
                    translationList.add(result.translation)
                }
                _translations.value = translationList
            } else {
                throw RuntimeException("Episodes is empty")
            }
        }
    }

    fun loadEpisodes(translationId: Int) {
        for (result in _episodeList.value!!) {
            if (translationId == result.translation.id) {
                if (result.seasons.isNotEmpty()) {
                    _episodes.value = result.seasons[result.lastSeason]?.episodes
                } else {
                    _episodes.value = mutableListOf(
                        Episode(
                            link = result.link,
                            screenshots =  result.screenshots,
                            episodeNumber = 1
                        )
                    )
                }
            }
        }
    }
}