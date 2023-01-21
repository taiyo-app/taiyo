package com.taiyoapp.taiyo.anime.data.mapper

import com.taiyoapp.taiyo.anime.domain.entity.Anime

class AnimeMapper {
    fun mapAnimeDtoToEntity(animeDto: com.taiyoapp.taiyo.anime.data.network.model.AnimeDto) = Anime(
        id = animeDto.id,
        title = animeDto.russian,
        image = BASE_IMAGE_URL + animeDto.animeImageDto?.original,
        status = animeDto.status,
        episodesAired = animeDto.episodesAired,
        episodesTotal = episodeTotalValid(animeDto)
    )

    private fun episodeTotalValid(
        animeDto: com.taiyoapp.taiyo.anime.data.network.model.AnimeDto,
    ) = if (animeDto.episodes == "0") "~" else animeDto.episodes

    companion object {
        private const val BASE_IMAGE_URL = "https://shikimori.one"
    }
}