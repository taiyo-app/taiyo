package com.taiyoapp.taiyo.anime.presentation.util

import android.os.Build
import androidx.annotation.RequiresApi
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.*


object DateFormatter {
    fun formatAiredOn(airedOn: String?): String {
        return if (airedOn != null && airedOn != "~") {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
                val date = LocalDate.parse(airedOn, formatter)
                val day = date.dayOfMonth
                val formattedDate = if (day == 1) {
                    val month = date.month.name
                    val year = date.year
                    val season = getSeasonFromMonth(month)
                    "$season $year"
                } else {
                    DateTimeFormatter.ofPattern("d MMM yyyy", locale)
                        .format(date)
                }
                return formattedDate
            } else {
                val formatter = SimpleDateFormat("yyyy-MM-dd", locale)
                val date = formatter.parse(airedOn)
                val day = SimpleDateFormat("d", locale).format(date!!).toInt()
                val dataFormatter = if (day == 1) {
                    SimpleDateFormat("LLLL yyyy", locale)
                } else {
                    SimpleDateFormat("d MMM yyyy", locale)
                }
                return dataFormatter.format(date)
            }
        } else {
            "скоро"
        }
    }

    private fun getSeasonFromMonth(month: String): String {
        return when (month) {
            in summer -> "лето"
            in autumn -> "осень"
            in winter -> "зима"
            in spring -> "весна"
            else -> "invalid month"
        }
    }

    fun formatNextEpisodeAt(nextEpisodeAt: String?): Long {
        return if (nextEpisodeAt != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val formattedTime = nextEpisodeAt.toDateISO_8601()
                    .toEpochSecond() * 1000 - System.currentTimeMillis()
                return if (formattedTime > 0) {
                    formattedTime
                } else {
                    invalidTime
                }
            } else {
                invalidTime
            }
        } else {
            invalidTime
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun String.toDateISO_8601(): ZonedDateTime {
        return ZonedDateTime.parse(this, DateTimeFormatter.ISO_DATE_TIME)
    }

    private val summer = arrayOf("JUNE", "JULY", "AUGUST")
    private val autumn = arrayOf("SEPTEMBER", "OCTOBER", "NOVEMBER")
    private val winter = arrayOf("DECEMBER", "JANUARY", "FEBRUARY")
    private val spring = arrayOf("MARCH", "APRIL", "MAY")

    private val locale = Locale("ru")
    private const val invalidTime = -1L
}

