package com.taiyoapp.taiyo.anime.presentation.viewmodel

import android.os.CountDownTimer
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.taiyoapp.taiyo.anime.data.repository.AnimeRepositoryImpl
import com.taiyoapp.taiyo.anime.domain.entity.AnimeDetail
import com.taiyoapp.taiyo.anime.domain.entity.Video
import com.taiyoapp.taiyo.anime.domain.usecase.GetAnimeDetailUseCase
import com.taiyoapp.taiyo.anime.domain.usecase.GetPosterUseCase
import com.taiyoapp.taiyo.anime.domain.usecase.GetVideoUseCase
import com.taiyoapp.taiyo.anime.presentation.util.DateFormatter
import kotlinx.coroutines.launch


class DetailViewModel : ViewModel() {
    private val repository = AnimeRepositoryImpl()

    private val getPosterUseCase = GetPosterUseCase(repository)
    private val getAnimeDetailUseCase = GetAnimeDetailUseCase(repository)
    private val getVideoUseCase = GetVideoUseCase(repository)

    private var _poster = MutableLiveData<String>()
    val poster: LiveData<String>
        get() = _poster

    private var _animeDetail = MutableLiveData<AnimeDetail>()
    val animeDetail: LiveData<AnimeDetail>
        get() = _animeDetail

    private var _videoList = MutableLiveData<List<Video>>()
    val videoList: LiveData<List<Video>>
        get() = _videoList

    private var _videoKind = MutableLiveData<List<Video>>()
    val videoKind: LiveData<List<Video>>
        get() = _videoKind

    private var timer: CountDownTimer? = null

    fun loadPoster(id: Int) {
        viewModelScope.launch {
            getPosterUseCase(id)
                .collect {
                    _poster.postValue(it)
                }
        }
    }

    fun loadAnimeDetail(id: Int) {
        viewModelScope.launch {
            getAnimeDetailUseCase(id)
                .collect {
                    _animeDetail.postValue(it)
                }
            getVideoUseCase(id)
                .collect {
                    _videoList.value = it
                }
        }
    }

    private fun formatAiredOn(airedOn: String): String {
        val formattedDate = DateFormatter.formatAiredOn(airedOn)
        return if (formattedDate[0] in '0'..'9') {
            formattedDate
        } else {
            formattedDate.substring(0, 1).uppercase() + formattedDate.substring(1)
        }
    }

    fun getInfoMapFromDetail(animeDetail: AnimeDetail): Map<String, String> {
        return mapOf(
            "Статус" to animeDetail.status,
            "Эпизоды" to animeDetail.episodesAired + " из " + animeDetail.episodesTotal,
            "Дата выхода" to formatAiredOn(animeDetail.airedOn),
            "Формат" to animeDetail.kind,
            "Длительность" to animeDetail.duration,
            "Студия" to animeDetail.studio
        )
    }

    fun getVideoKinds(videoList: List<Video>): List<Video> {
        val kindList = videoList
            .distinctBy { it.kind }
        val formattedKindList = mutableListOf<Video>()
        for (video in kindList) {
            val formattedkind = when (video.kind) {
                "pv" -> "Трейлеры"
                "character_trailer" -> "Персонажи"
                "episode_preview" -> "Превью"
                "op" -> "Опенинги"
                "ed" -> "Эндинги"
                "clip" -> "Клипы"
                "op_ed_clip" -> "Муз. клипы"
                else -> "Другое"
            }
            formattedKindList.add(video.copy(kind = formattedkind))
        }
        return formattedKindList
    }


    override fun onCleared() {
        super.onCleared()
        timer?.cancel()
    }
}