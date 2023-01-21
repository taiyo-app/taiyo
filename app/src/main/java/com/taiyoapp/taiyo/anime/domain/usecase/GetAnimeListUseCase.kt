package com.taiyoapp.taiyo.anime.domain.usecase

import com.taiyoapp.taiyo.anime.domain.repository.AnimeRepository

class GetAnimeListUseCase(
    private val repository: AnimeRepository,
) {
    suspend operator fun invoke(queryMap: HashMap<String, Any>) = repository.getAnimeList(queryMap)
}