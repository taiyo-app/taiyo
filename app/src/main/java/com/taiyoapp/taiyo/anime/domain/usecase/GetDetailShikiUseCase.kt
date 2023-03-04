package com.taiyoapp.taiyo.anime.domain.usecase

import com.taiyoapp.taiyo.anime.domain.repository.AnimeRepository

class GetDetailShikiUseCase(
    private val repository: AnimeRepository,
) {
    suspend operator fun invoke(id: Int) = repository.getDetailShiki(id)
}