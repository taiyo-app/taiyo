package com.taiyoapp.taiyo.anime.domain.usecase

import com.taiyoapp.taiyo.anime.domain.repository.AnimeRepository

class GetEpisodesUseCase(
    private val repository: AnimeRepository
) {
    suspend operator fun invoke(id: Int) = repository.getEpisodes(id)
}