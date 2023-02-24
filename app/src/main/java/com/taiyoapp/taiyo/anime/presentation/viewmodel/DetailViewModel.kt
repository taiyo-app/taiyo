package com.taiyoapp.taiyo.anime.presentation.viewmodel

import android.os.CountDownTimer
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.taiyoapp.taiyo.anime.data.repository.AnimeRepositoryImpl
import com.taiyoapp.taiyo.anime.domain.entity.AnimeDetail
import com.taiyoapp.taiyo.anime.domain.usecase.GetAnimeDetailUseCase
import com.taiyoapp.taiyo.anime.domain.usecase.GetPosterUseCase
import com.taiyoapp.taiyo.anime.presentation.util.DateFormatter
import kotlinx.coroutines.launch

class DetailViewModel : ViewModel() {
    private val repository = AnimeRepositoryImpl()

    private val getAnimeDetailUseCase = GetAnimeDetailUseCase(repository)
    private val getPosterUseCase = GetPosterUseCase(repository)

    private var _poster = MutableLiveData<String>()
    val poster: LiveData<String>
        get() = _poster

    private var _animeDetail = MutableLiveData<AnimeDetail>()
    val animeDetail: LiveData<AnimeDetail>
        get() = _animeDetail

    private var timer: CountDownTimer? = null

    private val _formattedTime = MutableLiveData<Pair<List<String>, List<Long>>>()
    val formattedTime: LiveData<Pair<List<String>, List<Long>>>
        get() = _formattedTime

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
        }
    }

    fun startTimer(timeUntilAiring: Long) {
        val milliseconds = timeUntilAiring * 1000L
        timer = object : CountDownTimer(milliseconds, 1000) {
            override fun onTick(timeUntilAiring: Long) {
                _formattedTime.value = formatTime(timeUntilAiring)
            }
            override fun onFinish() {}
        }
        timer?.start()
    }

    fun formatTime(timeUntilAiring: Long): Pair<List<String>, List<Long>> {
        val days = timeUntilAiring / 86400000
        val hours = timeUntilAiring % 86400000 / 3600000
        val minutes = timeUntilAiring % 86400000 % 3600000 / 60000
        val seconds = timeUntilAiring % 86400000 % 3600000 % 60000 / 1000
        val daysKey = when (days) {
            1L -> "День"
            in 2..4 -> "Дня"
            else -> "Дней"
        }
        val hoursKey = when (hours) {
            1L, 21L -> "Час"
            in 2..4, !in 0..21 -> "Часа"
            else -> "Часов"
        }
        val minutesKey =
            if (minutes == 1L || minutes > 20 && minutes % 10 == 1L)
                "Минута"
            else if (minutes in 2..4 || minutes > 20 && minutes % 10 > 1 && minutes % 10 < 5)
                "Минуты"
            else "Минут"
        val secondsKey =
            if (seconds == 1L || seconds > 20 && seconds % 10 == 1L)
                "Секунда"
            else if (seconds in 2..4 || seconds > 20 && seconds % 10 > 1 && seconds % 10 < 5)
                "Секунды"
            else "Секунд"
        val keys = listOf(daysKey, hoursKey, minutesKey, secondsKey)
        val values = listOf(days, hours, minutes, seconds)
        return Pair(keys, values)
    }

    fun formatAiredOn(airedOn: String): String {
        val formattedDate = DateFormatter.formatAiredOn(airedOn)
        return if (formattedDate[0] in '0'..'9') {
            formattedDate
        } else {
            formattedDate.substring(0, 1).uppercase() + formattedDate.substring(1)
        }
    }

    override fun onCleared() {
        super.onCleared()
        timer?.cancel()
    }
}