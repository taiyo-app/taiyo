package com.taiyoapp.taiyo.anime.presentation.util

import android.os.Build
import android.util.Log
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.*


object DateFormatter {
    fun formatAiredOn(airedOn: String?): String {
        return if (airedOn != null) {
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

    // TODO: неправильно отображается время
    fun formatNextEpisodeAt(nextEpisodeAt: String?): Long {
        return if (nextEpisodeAt != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val myDate = "2023-02-25T11:30:00.000+03:00"
                val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSz")
                val date = LocalDateTime.parse(myDate, formatter)
                Log.d("Milli", date.atOffset(ZoneOffset.UTC).toInstant().toEpochMilli().toString())
                Log.d("Milli", System.currentTimeMillis().toString())
                return date.atOffset(ZoneOffset.UTC).toInstant().toEpochMilli() - System.currentTimeMillis()
            } else {
                2000
            }
        } else {
            2000
        }
    }

    private val summer = arrayOf("JUNE", "JULY", "AUGUST")
    private val autumn = arrayOf("SEPTEMBER", "OCTOBER", "NOVEMBER")
    private val winter = arrayOf("DECEMBER", "JANUARY", "FEBRUARY")
    private val spring = arrayOf("MARCH", "APRIL", "MAY")

    private val locale = Locale("ru")
}

