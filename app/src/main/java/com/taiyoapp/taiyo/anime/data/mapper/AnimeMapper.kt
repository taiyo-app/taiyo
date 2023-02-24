package com.taiyoapp.taiyo.anime.data.mapper

import com.taiyoapp.taiyo.R
import com.taiyoapp.taiyo.anime.data.network.model.AnimeDto
import com.taiyoapp.taiyo.anime.data.network.model.DetailMALDto
import com.taiyoapp.taiyo.anime.data.network.model.DetailShikiDto
import com.taiyoapp.taiyo.anime.data.network.model.EpisodeListDto
import com.taiyoapp.taiyo.anime.data.network.model.EpisodeListDto.*
import com.taiyoapp.taiyo.anime.domain.entity.Anime
import com.taiyoapp.taiyo.anime.domain.entity.AnimeDetail
import com.taiyoapp.taiyo.anime.domain.entity.EpisodeList
import com.taiyoapp.taiyo.anime.presentation.util.DateFormatter
import java.util.regex.Pattern

class AnimeMapper {
    fun mapAnimeDtoToEntity(animeDto: AnimeDto) = Anime(
        id = animeDto.id,
        title = animeDto.russian ?: "~",
        image = BASE_IMAGE_URL + animeDto.animeImageDto?.original,
        status = animeDto.status ?: "~",
        episodesAired = animeDto.episodesAired ?: "~",
        episodesTotal = formatEpisodeTotal(animeDto.episodes ?: "~") ,
        airedOn = animeDto.airedOn ?: "~"
    )

    private fun formatEpisodeTotal(
        episodesTotal: String,
    ) = if (episodesTotal == "0") "~" else episodesTotal

    fun mapAnimeDetailDtoToEntity(detailShikiDto: DetailShikiDto) = AnimeDetail(
        id = detailShikiDto.id,
        name = detailShikiDto.name ?: "~",
        russian = detailShikiDto.russian ?: "~",
        kind = formatKind(detailShikiDto.kind),
        score = formatScore(detailShikiDto.score),
        status = formatStatus(detailShikiDto.status),
        episodesAired = detailShikiDto.episodesAired ?: "~",
        episodesTotal = formatEpisodeTotal(detailShikiDto.episodes ?: "~"),
        airedOn = detailShikiDto.airedOn ?: "~",
        duration = formatDuration(detailShikiDto.duration),
        description = formatDescription(detailShikiDto.description),
        nextEpisodeAt = DateFormatter.formatNextEpisodeAt(detailShikiDto.nextEpisodeAt),
        genres = mapGenreDtoToEntity(detailShikiDto.genres ?: listOf()),
        studio = formatStudios(detailShikiDto.studios)
    )

    private fun mapGenreDtoToEntity(
        genres: List<DetailShikiDto.GenreDto>,
    ): List<String> {
        return genres.map { it.russian ?: "~" }
    }

    private fun mapStudioDtoToEntity(
        studios: List<DetailShikiDto.StudioDto>?,
    ): List<String>? {
        return studios?.map { it.name ?: "~" }
    }

    fun mapPosterDtoToEntity(
        detailMALDto: DetailMALDto,
    ): String {
        return detailMALDto.mainPicture.large ?: "~"
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

    private fun formatScore(score: String?): String {
        return if (score == "0.0") {
            "~"
        } else {
            score?.take(3) ?: "~"
        }
    }

    private fun formatStatus(status: String?) = when (status) {
        "ongoing" -> "Онгоинг"
        "anons" -> "Анонс"
        "released" -> "Завершен"
        else -> "~"
    }

    private fun formatKind(kind: String?) = when (kind) {
        "tv" -> "Сериал"
        "movie" -> "Фильм"
        else -> "~"
    }

    private fun formatDuration(duration: Int?): String {
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

    private fun formatStudios(studios: List<DetailShikiDto.StudioDto>?): String {
        val studioList = mapStudioDtoToEntity(studios)
        return studioList?.get(0) ?: "${R.string.place_holder_symbol}"
    }

    private fun formatDescription(description: String?): String {
        return if (description != null) {
            val stringBuffer = StringBuffer()
            val patternSeparator = Pattern.compile("\\[.*?]|\r\n\r")
            val matcher = patternSeparator.matcher(description)
            while (matcher.find()) {
                matcher.appendReplacement(stringBuffer, "")
            }
            matcher.appendTail(stringBuffer)
            stringBuffer.toString()
        } else {
            "Описание отсутствует"
        }
    }

    companion object {
        private const val BASE_IMAGE_URL = "https://shikimori.one"
    }
}