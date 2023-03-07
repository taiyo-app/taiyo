package com.taiyoapp.taiyo.anime.data.mapper

import com.google.gson.GsonBuilder
import com.google.gson.JsonElement
import com.taiyoapp.taiyo.anime.data.network.model.*
import com.taiyoapp.taiyo.anime.data.network.model.EpisodesDto.*
import com.taiyoapp.taiyo.anime.domain.entity.*
import com.taiyoapp.taiyo.anime.presentation.util.DateFormatter
import java.util.regex.Pattern

class AnimeMapper {

    // Shikimori

    fun mapAnimeDtoToEntity(animeDto: AnimeDto) = Anime(
        id = animeDto.id,
        title = animeDto.russian ?: EMPTY_VALUE_STRING,
        image = BASE_IMAGE_URL + animeDto.imageDto?.original,
        status = animeDto.status ?: EMPTY_VALUE_STRING,
        episodesAired = animeDto.episodesAired ?: EMPTY_VALUE_STRING,
        episodesTotal = formatEpisodeTotal(animeDto.episodes ?: EMPTY_VALUE_STRING) ,
        airedOn = animeDto.airedOn ?: EMPTY_VALUE_STRING
    )

    fun mapDetailShikiDtoToEntity(detailShikiDto: DetailShikiDto) = DetailShiki(
        id = detailShikiDto.id,
        name = detailShikiDto.name ?: EMPTY_VALUE_STRING,
        russian = detailShikiDto.russian ?: EMPTY_VALUE_STRING,
        kind = formatKind(detailShikiDto.kind),
        score = formatScore(detailShikiDto.score),
        status = formatStatus(detailShikiDto.status),
        episodesAired = formatEpisodesAired(
            detailShikiDto.episodesAired ?: EMPTY_VALUE_STRING
        ),
        episodesTotal = formatEpisodeTotal(
            detailShikiDto.episodes ?: EMPTY_VALUE_STRING
        ),
        airedOn = detailShikiDto.airedOn ?: EMPTY_VALUE_STRING,
        duration = formatDuration(detailShikiDto.duration),
        description = formatDescription(detailShikiDto.description),
        ratesScoresStats = formatScoreForChart(
            mapRatesScoresStatsDtoToEntity(detailShikiDto.ratesScoresStats)
        ),
        nextEpisodeAt = DateFormatter.formatNextEpisodeAt(detailShikiDto.nextEpisodeAt),
        genres = mapGenreDtoToEntity(detailShikiDto.genres ?: listOf()),
        studio = formatStudios(detailShikiDto.studios)
    )

    private fun formatEpisodesAired(
        episodesAired: String,
    ) = if (episodesAired == "0") EMPTY_VALUE_STRING else episodesAired

    private fun formatEpisodeTotal(
        episodesTotal: String,
    ) = if (episodesTotal == "0") EMPTY_VALUE_STRING else episodesTotal

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

    fun mapJsonArrayScreenshotsDtoToEntity(jsonElement: JsonElement): List<Screenshot> {
        val jsonArray = jsonElement.asJsonArray
        val screenshots = mutableListOf<Screenshot>()
        for (jsonElementItem in jsonArray) {
            val jsonObject = jsonElementItem.asJsonObject
            val gson = GsonBuilder().create()
            val screenshot = mapScreenshotDtoToEntity(
                gson.fromJson(
                    jsonObject,
                    ScreenshotDto::class.java
                )
            )
            screenshots.add(screenshot)
        }
        return screenshots
    }

    private fun mapScreenshotDtoToEntity(screenshotDto: ScreenshotDto) = Screenshot(
        original = BASE_IMAGE_URL + screenshotDto.original
    )

    fun mapJsonArraySimilarDtoToEntity(jsonElement: JsonElement): List<Anime> {
        val jsonArray = jsonElement.asJsonArray
        val similar = mutableListOf<Anime>()
        for (jsonElementItem in jsonArray) {
            val jsonObject = jsonElementItem.asJsonObject
            val gson = GsonBuilder().create()
            val screenshot = mapAnimeDtoToEntity(
                gson.fromJson(
                    jsonObject,
                    AnimeDto::class.java
                )
            )
            similar.add(screenshot)
        }
        return similar
    }

    private fun formatScore(score: String?): String {
        return if (score == "0.0" || score == null) {
            EMPTY_VALUE_INT.toString()
        } else {
            score.take(4)
        }
    }

    private fun mapVideoDtoToEntity(videoDto: VideoDto) = Video(
        id = videoDto.id,
        url = videoDto.url ?: EMPTY_VALUE_STRING,
        imageUrl = videoDto.imageUrl ?: EMPTY_VALUE_STRING,
        playerUrl = videoDto.playerUrl ?: EMPTY_VALUE_STRING,
        name = videoDto.name ?: EMPTY_VALUE_STRING,
        kind = videoDto.kind ?: EMPTY_VALUE_STRING,
        hosting = videoDto.hosting ?: EMPTY_VALUE_STRING
    )

    private fun mapRatesScoresStatsDtoToEntity(
        mapRatesScoresStatsDto: List<DetailShikiDto.RatesScoresStatsDto>?,
    ): LinkedHashMap<String, Float> {
        if (mapRatesScoresStatsDto != null) {
            val scoreStatsMap = mutableMapOf<String, Float>() as LinkedHashMap<String, Float>
            for (key in 1 until 11) {
                scoreStatsMap[key.toString()] = EMPTY_VALUE_FLOAT
            }
            mapRatesScoresStatsDto.let { it ->
                it.map {
                    DetailShiki.RatesScoresStats(
                        name = it.name ?: EMPTY_VALUE_STRING,
                        value = it.value?.toFloat() ?: EMPTY_VALUE_FLOAT
                    )
                }
                .forEach {
                    scoreStatsMap[it.name] = it.value
                }
            }
            return scoreStatsMap
        } else {
            return mutableMapOf<String, Float>() as LinkedHashMap<String, Float>
        }
    }

    // преобразуем значения из 10 в 5 систему оценивания
    private fun formatScoreForChart(
        scoreStatsMap: LinkedHashMap<String, Float>
    ): LinkedHashMap<String, Float> {
        return if (scoreStatsMap.size != EMPTY_VALUE_INT) {
            val sum = scoreStatsMap.values.sum()
            val scoreStats = mutableMapOf<String, Float>() as LinkedHashMap<String, Float>
            // проходимся по 5 звездам
            var starCounter = 1
            for (key in 1 until 11 step 2) {
                val sumOfPairStats = scoreStatsMap[key.toString()]!! + scoreStatsMap[(key + 1).toString()]!!
                // вычисляем процент от суммы. Прибавляем 5, чтобы отрисовать минимальные значения
                val percentOfPairStats = sumOfPairStats / sum * 100 + 5
                scoreStats[starCounter++.toString()] = percentOfPairStats
            }
            scoreStats
        } else {
            scoreStatsMap
        }
    }

    private fun mapGenreDtoToEntity(
        genres: List<DetailShikiDto.GenreDto>,
    ): List<String> {
        return genres.map { it.russian ?: EMPTY_VALUE_STRING }
    }

    private fun mapStudioDtoToEntity(
        studios: List<DetailShikiDto.StudioDto>?,
    ): List<String>? {
        return studios?.map { it.name ?: EMPTY_VALUE_STRING }
    }

    private fun mapStatusDtoToEntity(status: DetailMalDto.Statistics.Status) = DetailMal.Statistics.Status(
        watching = status.watching ?: EMPTY_VALUE_STRING,
        completed = status.completed ?: EMPTY_VALUE_STRING,
        onHold = status.onHold ?: EMPTY_VALUE_STRING,
        dropped = status.dropped ?: EMPTY_VALUE_STRING,
        planToWatch = status.planToWatch ?: EMPTY_VALUE_STRING
    )

    fun mapEpisodeListDtoToEntity(episodesDto: EpisodesDto) = Episodes(
        results = episodesDto.results?.map { mapResultDtoToEntity(it) } ?: listOf()
    )

    private fun mapResultDtoToEntity(resultDto: ResultDto): Episodes.Result {
        episodeNumber = 1
        return Episodes.Result(
            link = resultDto.link?.let { "https:" + resultDto.link } ?: EMPTY_VALUE_STRING ,
            translation = mapTranslationDtoToEntity(
                resultDto.translation ?: TranslationDto(
                    EMPTY_VALUE_INT,
                    EMPTY_VALUE_STRING,
                    EMPTY_VALUE_STRING
                ),
                resultDto.episodesCount ?: EMPTY_VALUE_INT
            ),
            screenshots = resultDto.screenshots?.toList() ?: listOf(),
            episodesCount = resultDto.episodesCount ?: EMPTY_VALUE_INT,
            seasons = mapSeasonsDtoToEntity(resultDto.seasons ?: mapOf()),
            lastSeason = resultDto.lastSeason ?: EMPTY_VALUE_INT
        )
    }

    private fun mapTranslationDtoToEntity(translationDto: TranslationDto, episodesCount: Int) =
        Episodes.Translation(
            id = translationDto.id ?: EMPTY_VALUE_INT,
            title = translationDto.title ?: EMPTY_VALUE_STRING,
            type = translationDto.type ?: EMPTY_VALUE_STRING,
            episodesCount = episodesCount
        )

    private fun mapSeasonsDtoToEntity(seasonsDto: Map<Int, SeasonDto>): Map<Int, Episodes.Season> {
        val seasons = mutableMapOf<Int, Episodes.Season>()
        for (seasonDto in seasonsDto) {
            seasons[seasonDto.key] = Episodes.Season(
                episodes = seasonDto.value.episodes?.map {
                    mapEpisodeDtoToEntity(it.value)
                } ?: emptyList()
            )
        }
        return seasons
    }

    private var episodeNumber = 1
    private fun mapEpisodeDtoToEntity(episodeDto: EpisodeDto?) = Episodes.Episode(
        link = episodeDto?.link ?: EMPTY_VALUE_STRING,
        screenshots = episodeDto?.screenshots ?: emptyList(),
        episodeNumber = episodeNumber++
    )

    private fun formatStatus(status: String?) = when (status) {
        "ongoing" -> "Онгоинг"
        "anons" -> "Анонс"
        "released" -> "Завершен"
        else -> EMPTY_VALUE_STRING
    }

    private fun formatKind(kind: String?) = when (kind) {
        "tv" -> "Сериал"
        "movie" -> "Фильм"
        else -> EMPTY_VALUE_STRING
    }

    private fun formatDuration(duration: Int?): String {
        return if (duration != null) {
            val hours = duration / 60
            val minutes = duration % 60
            if (hours > 0) {
                "$hours ч. $minutes мин."
            } else if (minutes == 0) {
                "$EMPTY_VALUE_STRING мин."
            } else {
                "$minutes мин."
            }
        } else {
            EMPTY_VALUE_STRING
        }
    }

    private fun formatStudios(studios: List<DetailShikiDto.StudioDto>?): String {
        val studioList = mapStudioDtoToEntity(studios)
        return if (studioList != null && studioList.isNotEmpty()) {
            studioList[0]
        } else {
            EMPTY_VALUE_STRING
        }
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

    // MyAnimeList

    fun mapDetailMalDtoToEntity(detailMalDto: DetailMalDto) = DetailMal(
        mainPicture = mapMainPictureDtoToEntity(
            detailMalDto.mainPicture ?: DetailMalDto.MainPicture(
                EMPTY_VALUE_STRING
            )
        ),
        numScoringUsers = (detailMalDto.numScoringUsers) ?: 0,
        statistics = mapStatisticsDtoToEntity(
            detailMalDto.statistics ?: DetailMalDto.Statistics(
                status = null,
                numListUsers = EMPTY_VALUE_INT
            )
        )
    )

    private fun mapMainPictureDtoToEntity(mainPicture: DetailMalDto.MainPicture) = DetailMal.MainPicture(
        large = mainPicture.large ?: EMPTY_VALUE_STRING
    )

    private fun mapStatisticsDtoToEntity(statistics: DetailMalDto.Statistics) = DetailMal.Statistics(
        status = mapStatusDtoToEntity(
            statistics.status ?: DetailMalDto.Statistics.Status(
                watching = EMPTY_VALUE_STRING,
                completed = EMPTY_VALUE_STRING,
                onHold = EMPTY_VALUE_STRING,
                dropped = EMPTY_VALUE_STRING,
                planToWatch = EMPTY_VALUE_STRING
            )
        ),
        numListUsers = statistics.numListUsers ?: EMPTY_VALUE_INT
    )

    companion object {
        private const val BASE_IMAGE_URL = "https://shikimori.one"

        private const val EMPTY_VALUE_STRING = "~"
        private const val EMPTY_VALUE_INT = 0
        private const val EMPTY_VALUE_FLOAT = 0F
    }
}