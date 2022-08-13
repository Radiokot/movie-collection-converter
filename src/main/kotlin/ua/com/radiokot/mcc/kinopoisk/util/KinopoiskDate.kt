package ua.com.radiokot.mcc.kinopoisk.util

import java.time.format.DateTimeFormatter

object KinopoiskDate {
    /**
     * Kinopoisk uses this date format despite client's OS preferences.
     */
    private const val PATTERN = "dd.MM.yyyy, HH:mm"

    val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern(PATTERN)
}