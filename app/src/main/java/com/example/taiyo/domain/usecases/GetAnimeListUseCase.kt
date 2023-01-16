package com.example.taiyo.domain.usecases

import com.example.taiyo.domain.repository.AnimeRepository

class GetAnimeListUseCase(
    private val repository: AnimeRepository,
) {
    suspend operator fun invoke(queryMap: HashMap<String, Any>) = repository.getAnimeList(queryMap)
}