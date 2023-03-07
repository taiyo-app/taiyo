package com.taiyoapp.taiyo.anime.presentation.viewmodel

import android.os.CountDownTimer
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.taiyoapp.taiyo.anime.data.repository.AnimeRepositoryImpl
import com.taiyoapp.taiyo.anime.domain.entity.*
import com.taiyoapp.taiyo.anime.domain.usecase.*
import com.taiyoapp.taiyo.anime.presentation.util.DateFormatter
import kotlinx.coroutines.launch


class DetailViewModel : ViewModel() {
    private val repository = AnimeRepositoryImpl()

    private val getDetailMalUseCase = GetDetailMalUseCase(repository)
    private val getDetailShikiUseCase = GetDetailShikiUseCase(repository)
    private val getVideoUseCase = GetVideoUseCase(repository)
    private val getScreenshotsUseCase = GetScreenshotsUseCase(repository)
    private val getSimilarUseCase = GetSimilarUseCase(repository)
    private val getEpisodesUseCase = GetEpisodesUseCase(repository)

    private var _detailMal = MutableLiveData<DetailMal>()
    val detailMAL: LiveData<DetailMal>
        get() = _detailMal

    private var _DetailShiki = MutableLiveData<DetailShiki>()
    val detailShiki: LiveData<DetailShiki>
        get() = _DetailShiki

    private var _video = MutableLiveData<List<Video>>()
    val video: LiveData<List<Video>>
        get() = _video

    private var _videoKind = MutableLiveData<List<Video>>()
    val videoKind: LiveData<List<Video>>
        get() = _videoKind

    private var _screenshots = MutableLiveData<List<Screenshot>>()
    val screenshots: LiveData<List<Screenshot>>
        get() = _screenshots

    private var _similar = MutableLiveData<List<Anime>>()
    val similar: LiveData<List<Anime>>
        get() = _similar

    private var timer: CountDownTimer? = null

    private var _containsEpisodes = MutableLiveData<Boolean>()
    val containsEpisodes: LiveData<Boolean>
        get() = _containsEpisodes


    private val _episode = MutableLiveData<List<Episodes.Result>>()


    fun loadDetailMal(id: Int) {
        viewModelScope.launch {
            getDetailMalUseCase(id)
                .collect {
                    _detailMal.postValue(it)
                }
        }
    }

    fun loadDetailShiki(id: Int) {
        viewModelScope.launch {
            getDetailShikiUseCase(id)
                .collect {
                    _DetailShiki.postValue(it)
                }
            getVideoUseCase(id)
                .collect {
                    _video.value = it
                }
            getScreenshotsUseCase(id)
                .collect {
                    _screenshots.value = it
                }
            getSimilarUseCase(id)
                .collect {
                    _similar.value = it
                }
        }
    }

    fun loadEpisodesKodik(id: Int) {
        viewModelScope.launch {
            val episodes = getEpisodesUseCase(id)
            if (episodes.isNotEmpty()) {
                _containsEpisodes.value = true
            } else {
                _containsEpisodes.value = false
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

    fun getInfoMapFromDetail(detailShiki: DetailShiki): Map<String, String> {
        val episodesNumber = if (detailShiki.episodesAired == detailShiki.episodesTotal) {
            detailShiki.episodesTotal
        } else if (detailShiki.episodesAired == "~") {
            detailShiki.episodesTotal
        }
        else {
            detailShiki.episodesAired + " из " + detailShiki.episodesTotal
        }
        return mapOf(
            "Статус" to detailShiki.status,
            "Эпизоды" to episodesNumber,
            "Дата выхода" to formatAiredOn(detailShiki.airedOn),
            "Формат" to detailShiki.kind,
            "Длительность" to detailShiki.duration,
            "Студия" to detailShiki.studio
        )
    }

    fun getVideoKinds(video: List<Video>): List<Video> {
        val videoByKinds = video
            .distinctBy { it.kind }
        return videoByKinds
    }

    fun getVideoByKind(kind: String): List<Video> {
        val video = _video.value!!.filter {
            it.kind == kind
        }
        return video
    }

    fun getStatusStatsForCharts(status: DetailMal.Statistics.Status): List<Float> {
        val amount = with(status) {
            (watching.toFloat() + completed.toFloat() + planToWatch.toFloat()
                    + onHold.toFloat() + dropped.toFloat())
        }
        val watching = status.watching.toFloat()
        val playToWatch = status.planToWatch.toFloat()
        val completed = status.completed.toFloat()
        val onHold = status.onHold.toFloat()
        val dropped = status.dropped.toFloat()
        return listOf(watching, playToWatch, completed, onHold, dropped, amount)

    }

    override fun onCleared() {
        super.onCleared()
        timer?.cancel()
    }
}