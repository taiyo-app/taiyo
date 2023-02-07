package com.taiyoapp.taiyo.anime.data.mapper

import com.taiyoapp.taiyo.anime.data.network.model.AnimeDetailDto
import com.taiyoapp.taiyo.anime.data.network.model.AnimeDto
import com.taiyoapp.taiyo.anime.data.network.model.EpisodeListDto
import com.taiyoapp.taiyo.anime.data.network.model.EpisodeListDto.*
import com.taiyoapp.taiyo.anime.domain.entity.Anime
import com.taiyoapp.taiyo.anime.domain.entity.AnimeDetail
import com.taiyoapp.taiyo.anime.domain.entity.EpisodeList

class AnimeMapper {
    fun mapAnimeDtoToEntity(animeDto: AnimeDto) = Anime(
        id = animeDto.id,
        title = animeDto.russian,
        image = BASE_IMAGE_URL + animeDto.animeImageDto?.original,
        status = animeDto.status,
        episodesAired = animeDto.episodesAired,
        episodesTotal = episodeTotalValid(animeDto.episodes)
    )

    private fun episodeTotalValid(
        episodesTotal: String?,
    ) = if (episodesTotal == "0") "~" else episodesTotal

    fun mapAnimeDetailDtoToEntity(animeDetailDto: AnimeDetailDto) = AnimeDetail(
        id = animeDetailDto.id,
        name = animeDetailDto.name,
        russian = animeDetailDto.russian,
        kind = animeDetailDto.kind,
        status = animeDetailDto.status,
        episodesAired = animeDetailDto.episodesAired,
        episodesTotal = episodeTotalValid(animeDetailDto.episodes),
        releasedOn = animeDetailDto.releasedOn,
        duration = animeDetailDto.duration,
        description = animeDetailDto.description,
        nextEpisodeAt = animeDetailDto.nextEpisodeAt,
        genres = mapGenreDtoToEntity(animeDetailDto.genres),
        studios = mapStudioDtoToEntity(animeDetailDto.studios)
    )

    private fun mapGenreDtoToEntity(
        genres: List<AnimeDetailDto.GenreDto>?,
    ): List<AnimeDetail.Genre>? {
        return genres?.map { AnimeDetail.Genre(it.russian) }
    }

    private fun mapStudioDtoToEntity(
        studios: List<AnimeDetailDto.StudioDto>?,
    ): List<AnimeDetail.Studio>? {
        return studios?.map { AnimeDetail.Studio(it.name) }
    }

    fun mapEpisodeListDtoToEntity(episodeListDto: EpisodeListDto) = EpisodeList(
        results = episodeListDto.results.map { mapResultDtoToEntity(it) }
    )

    private fun mapResultDtoToEntity(resultDto: ResultDto) = EpisodeList.Result(
        translation = mapTranslationDtoToEntity(resultDto.translation),
        seasons = resultDto.seasons.map { mapSeasonDtoToEntity(it.value) }
    )

    private fun mapTranslationDtoToEntity(translationDto: TranslationDto) =
        EpisodeList.Translation(
            id = translationDto.id,
            title = translationDto.title,
            type = translationDto.type
        )

    private fun mapSeasonDtoToEntity(seasonDto: SeasonDto) = EpisodeList.Season(
        episodes = seasonDto.episodes.map {
            mapEpisodeDtoToEntity(it.value)
        }
    )

    private fun mapEpisodeDtoToEntity(episodeDto: EpisodeDto) = EpisodeList.Episode(
        link = episodeDto.link,
        screenshots = episodeDto.screenshots
    )

    companion object {
        private const val BASE_IMAGE_URL = "https://shikimori.one"
    }
}