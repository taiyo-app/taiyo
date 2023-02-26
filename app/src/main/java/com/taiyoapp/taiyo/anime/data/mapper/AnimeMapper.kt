package com.taiyoapp.taiyo.anime.data.mapper

import com.google.gson.GsonBuilder
import com.google.gson.JsonElement
import com.taiyoapp.taiyo.anime.data.network.model.*
import com.taiyoapp.taiyo.anime.data.network.model.EpisodeListDto.*
import com.taiyoapp.taiyo.anime.domain.entity.*
import com.taiyoapp.taiyo.anime.presentation.util.DateFormatter
import java.util.regex.Pattern

class AnimeMapper {
    fun mapAnimeDtoToEntity(animeDto: AnimeDto) = Anime(
        id = animeDto.id,
        title = animeDto.russian ?: EMPTY_VALUE,
        image = BASE_IMAGE_URL + animeDto.animeImageDto?.original,
        status = animeDto.status ?: EMPTY_VALUE,
        episodesAired = animeDto.episodesAired ?: EMPTY_VALUE,
        episodesTotal = formatEpisodeTotal(animeDto.episodes ?: EMPTY_VALUE) ,
        airedOn = animeDto.airedOn ?: EMPTY_VALUE
    )

    private fun formatEpisodeTotal(
        episodesTotal: String,
    ) = if (episodesTotal == "0") EMPTY_VALUE else episodesTotal

    fun mapAnimeDetailDtoToEntity(detailShikiDto: DetailShikiDto) = AnimeDetail(
        id = detailShikiDto.id,
        name = detailShikiDto.name ?: EMPTY_VALUE,
        russian = detailShikiDto.russian ?: EMPTY_VALUE,
        kind = formatKind(detailShikiDto.kind),
        score = formatScore(detailShikiDto.score),
        status = formatStatus(detailShikiDto.status),
        episodesAired = detailShikiDto.episodesAired ?: EMPTY_VALUE,
        episodesTotal = formatEpisodeTotal(detailShikiDto.episodes ?: EMPTY_VALUE),
        airedOn = detailShikiDto.airedOn ?: EMPTY_VALUE,
        duration = formatDuration(detailShikiDto.duration),
        description = formatDescription(detailShikiDto.description),
        nextEpisodeAt = DateFormatter.formatNextEpisodeAt(detailShikiDto.nextEpisodeAt),
        genres = mapGenreDtoToEntity(detailShikiDto.genres ?: listOf()),
        studio = formatStudios(detailShikiDto.studios)
    )

    fun mapJsonArrayVideoDtoToEntity(jsonElement: JsonElement): List<Video> {
        val jsonArray = jsonElement.asJsonArray
        val videoList = mutableListOf<Video>()
        for (jsonElementItem in jsonArray) {
            val jsonObject = jsonElementItem.asJsonObject
            val gson = GsonBuilder().create()
            val videoItem = mapVideoDtoToEntity(
                gson.fromJson(
                    jsonObject,
                    VideoDto::class.java
                )
            )
            videoList.add(videoItem)
        }
        return videoList
    }

    private fun mapVideoDtoToEntity(videoDto: VideoDto) = Video(
        id = videoDto.id,
        url = videoDto.url ?: EMPTY_VALUE,
        imageUrl = videoDto.imageUrl ?: EMPTY_VALUE,
        playerUrl = videoDto.playerUrl ?: EMPTY_VALUE,
        name = videoDto.playerUrl ?: EMPTY_VALUE,
        kind = videoDto.kind ?: EMPTY_VALUE,
        hosting = videoDto.hosting ?: EMPTY_VALUE
    )

    private fun mapGenreDtoToEntity(
        genres: List<DetailShikiDto.GenreDto>,
    ): List<String> {
        return genres.map { it.russian ?: EMPTY_VALUE }
    }

    private fun mapStudioDtoToEntity(
        studios: List<DetailShikiDto.StudioDto>?,
    ): List<String>? {
        return studios?.map { it.name ?: EMPTY_VALUE }
    }

    fun mapPosterDtoToEntity(
        detailMALDto: DetailMALDto,
    ): String {
        return detailMALDto.mainPicture.large ?: EMPTY_VALUE
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
            EMPTY_VALUE
        } else {
            score?.take(3) ?: EMPTY_VALUE
        }
    }

    private fun formatStatus(status: String?) = when (status) {
        "ongoing" -> "Онгоинг"
        "anons" -> "Анонс"
        "released" -> "Завершен"
        else -> EMPTY_VALUE
    }

    private fun formatKind(kind: String?) = when (kind) {
        "tv" -> "Сериал"
        "movie" -> "Фильм"
        else -> EMPTY_VALUE
    }

    private fun formatDuration(duration: Int?): String {
        return if (duration != null) {
            val hours = duration / 60
            val minutes = duration % 60
            if (hours > 0) {
                "$hours ч. $minutes мин."
            } else if (minutes == 0) {
                "$EMPTY_VALUE мин."
            } else {
                "$minutes мин."
            }
        } else {
            EMPTY_VALUE
        }
    }

    private fun formatStudios(studios: List<DetailShikiDto.StudioDto>?): String {
        val studioList = mapStudioDtoToEntity(studios)
        return studioList?.get(0) ?: EMPTY_VALUE
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

        private const val EMPTY_VALUE = "~"
    }
}