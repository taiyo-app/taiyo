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