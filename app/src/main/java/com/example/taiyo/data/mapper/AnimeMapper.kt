package com.example.taiyo.data.mapper

import com.example.taiyo.data.network.model.AnimeDto
import com.example.taiyo.domain.entity.Anime

class AnimeMapper {
    fun mapAnimeDtoToEntity(animeDto: AnimeDto) = Anime(
        id = animeDto.id,
        title = animeDto.russian,
        image = BASE_IMAGE_URL + animeDto.animeImageDto?.original,
        status = animeDto.status,
        episodesAired = animeDto.episodesAired,
        episodesTotal = episodeTotalValid(animeDto)
    )

    private fun episodeTotalValid(
        animeDto: AnimeDto
    ) = if (animeDto.episodes == "0") "~" else animeDto.episodes

    companion object {
        private const val BASE_IMAGE_URL = "https://shikimori.one"
    }
}