package com.taiyoapp.taiyo.anime.presentation.viewmodel

import android.os.CountDownTimer
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.taiyoapp.taiyo.MediaQuery
import com.taiyoapp.taiyo.R
import com.taiyoapp.taiyo.anime.data.repository.AnimeRepositoryImpl
import com.taiyoapp.taiyo.anime.domain.entity.AnimeDetail
import com.taiyoapp.taiyo.anime.domain.usecase.GetAnimeDetailUseCase
import com.taiyoapp.taiyo.anime.domain.usecase.GetAnimeMediaUseCase
import kotlinx.coroutines.launch
import java.util.regex.Pattern

class DetailViewModel : ViewModel() {
    private val repository = AnimeRepositoryImpl()

    private val getAnimeDetailUseCase = GetAnimeDetailUseCase(repository)
    private val getAnimeMediaUseCase = GetAnimeMediaUseCase(repository)

    private var _animeDetail = MutableLiveData<AnimeDetail>()
    val animeDetail: LiveData<AnimeDetail>
        get() = _animeDetail

    private var _animeMedia = MutableLiveData<MediaQuery.Data?>()
    val animeMedia: LiveData<MediaQuery.Data?>
        get() = _animeMedia

    private var timer: CountDownTimer? = null

    private val _formattedTime = MutableLiveData<Pair<List<String>, List<Long>>>()
    val formattedTime: LiveData<Pair<List<String>, List<Long>>>
        get() = _formattedTime

    fun loadAnimeDetail(id: Int) {
        viewModelScope.launch {
            getAnimeDetailUseCase(id)
                .collect {
                    _animeDetail.value = it
                }
        }
    }

    fun loadAnimeMedia(id: Int) {
        viewModelScope.launch {
            _animeMedia.postValue(getAnimeMediaUseCase(id))
        }
    }

    fun parseStatus(status: String?): String {
        return when (status) {
            "ongoing" -> "Онгоинг"
            "anons" -> "Скоро"
            else -> "Завершен"
        }
    }

    fun startTimer(timeUntilAiring: Long) {
        if (_animeMedia.value?.Media?.airingSchedule?.nodes != null) {
            val milliseconds = timeUntilAiring * 1000L
            timer = object : CountDownTimer(milliseconds, 1000) {
                override fun onTick(timeUntilAiring: Long) {
                    _formattedTime.value = formatTime(timeUntilAiring)
                }

                override fun onFinish() {}
            }
            timer?.start()
        }
    }

    // PRESS "F"
    fun formatTime(timeUntilAiring: Long): Pair<List<String>, List<Long>> {
        val days = timeUntilAiring / 86400000
        val hours = timeUntilAiring % 86400000 / 3600000
        val minutes = timeUntilAiring % 86400000 % 3600000 / 60000
        val seconds = timeUntilAiring % 86400000 % 3600000 % 60000 / 1000
        // max 7 days
        val daysKey = when (days) {
            1L -> "ДЕНЬ"
            in 2..4 -> "ДНЯ"
            else -> "ДНЕЙ"
        }
        // max 24 hours
        val hoursKey = when (hours) {
            1L, 21L -> "ЧАС"
            in 2..4, !in 0..21 -> "ЧАСА"
            else -> "ЧАСОВ"
        }
        // max 60 minutes
        val minutesKey =
            if (minutes == 1L || minutes > 20 && minutes % 10 == 1L)
                "МИНУТА"
            else if (minutes in 2..4 || minutes > 20 && minutes % 10 > 1 && minutes % 10 < 5)
                "МИНУТЫ"
            else "МИНУТ"
        // max 60 secs
        val secondsKey =
            if (seconds == 1L || seconds > 20 && seconds % 10 == 1L)
                "СЕКУНДА"
            else if (seconds in 2..4 || seconds > 20 && seconds % 10 > 1 && seconds % 10 < 5)
                "СЕКУНДЫ"
            else "СЕКУНД"
        val keys = listOf(daysKey, hoursKey, minutesKey, secondsKey)
        val values = listOf(days, hours, minutes, seconds)
        return Pair(keys, values)
    }

    fun formatRating(rating: String?): String {
        return if (rating != null) {
            "${rating[0]}.${rating[1]}"
        } else {
            "${R.string.episodes_separator}"
        }
    }

    fun formatKind(kind: String?): String {
        return when (kind) {
            "tv" -> "Сериал"
            "movie" -> "Фильм"
            else -> "${R.string.place_holder_symbol}"
        }
    }

    fun formatDuration(duration: Int?): String {
        return if (duration != null) {
            val hours = duration / 60
            val minutes = duration % 60
            if (hours > 0) {
                "$hours ч. $minutes мин."
            } else {
                "$minutes мин."
            }
        } else {
            "${R.string.place_holder_symbol}"
        }
    }

    fun formatStudio(studios: List<AnimeDetail.Studio>?) =
        studios?.get(0)?.name ?: "${R.string.place_holder_symbol}"

    fun formatSeason(season: String?, seasonYear: Int?): String {
        return if (season != null && seasonYear != null) {
            val translateSeason = when (season) {
                "WINTER" -> "Зима"
                "SPRING" -> "Весна"
                "SUMMER" -> "Лето"
                else -> "Осень"
            }
            "$translateSeason $seasonYear"
        } else {
            "${R.string.place_holder_symbol}"
        }
    }

    fun formatDescription(description: String): String {
        val stringBuffer = StringBuffer()
        val patternSeparator = Pattern.compile("\\[.*?]|\r\n\r")
        val matcher = patternSeparator.matcher(description)
        while (matcher.find()) {
            matcher.appendReplacement(stringBuffer, "")
        }
        matcher.appendTail(stringBuffer)
        return stringBuffer.toString()
    }

    override fun onCleared() {
        super.onCleared()
        timer?.cancel()
    }
}