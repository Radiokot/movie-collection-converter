package ua.com.radiokot.mcc.kinopoisk.rating

import org.jsoup.Jsoup
import ua.com.radiokot.mcc.kinopoisk.rating.model.KinopoiskExistingMovieRating
import java.io.ByteArrayInputStream
import java.io.File
import java.io.InputStream

/**
 * Parses profile votes page HTML, e.g. https://www.kinopoisk.ru/user/13562461/votes/,
 * to the chronologically ordered list of [KinopoiskExistingMovieRating].
 * Performs no HTTP calls.
 */
class KinopoiskRatingsHtmlParser {
    fun parse(ratingPageFile: File): List<KinopoiskExistingMovieRating> =
        parse(ratingPageFile.inputStream())

    fun parse(ratingPageHtml: String): List<KinopoiskExistingMovieRating> =
        parse(ByteArrayInputStream(ratingPageHtml.toByteArray(Charsets.UTF_8)))

    fun parse(ratingPageInputStream: InputStream): List<KinopoiskExistingMovieRating> {
        return Jsoup.parse(
            ratingPageInputStream,
            Charsets.UTF_8.name(),
            "https://bk.kp.yandex.net/"
        )
            .selectFirst("div.profileFilmsList")
            .let { checkNotNull(it) { "Can't find .profileFilmsList table" } }
            .select("div.item")
            .map(KinopoiskExistingMovieRating.Companion::fromProfileFilmsListItem)
            .sortedBy(KinopoiskExistingMovieRating::date)
    }
}