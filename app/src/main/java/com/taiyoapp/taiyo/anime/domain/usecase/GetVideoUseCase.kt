package com.taiyoapp.taiyo.anime.domain.usecase

import com.taiyoapp.taiyo.anime.domain.repository.AnimeRepository

class GetVideoUseCase(
    private val repository: AnimeRepository,
) {
    suspend operator fun invoke(id: Int) = repository.getVideo(id)
}