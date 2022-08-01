package ua.com.radiokot.mcc.kinopoisk.movie.model

import org.jsoup.nodes.Element
import ua.com.radiokot.mcc.base.movie.model.ExistingMovie

data class KinopoiskExistingMovie(
    val russianName: String,
    override val convenientName: String,
    override val releaseYear: Int,
    val url: String,
) : ExistingMovie {

    companion object {
        /**
         * Creates movie from a list item from a votes page,
         * e.g. https://www.kinopoisk.ru/user/13562461/votes/
         */
        fun fromProfileFilmsListItem(item: Element): KinopoiskExistingMovie {
            // 1 – russian name, 2 – release date.
            val russianNameAndReleaseYearRegex = "^(.+)\\s\\(.*?(\\d{4}).*?\\)\$".toRegex()

            val linkElement = checkNotNull(item.select("div.nameRus a")) {
                "Can't find link in .nameRus element.\n" +
                        "$item"
            }

            val url = linkElement.attr("href")

            val russianNameAndReleaseYearMatch =
                checkNotNull(russianNameAndReleaseYearRegex.find(linkElement.text())) {
                    "Can't parse name and release year.\n" +
                            "$item"
                }
                    .groupValues

            val russianName = russianNameAndReleaseYearMatch[1]
            val releaseYear = russianNameAndReleaseYearMatch[2].toInt()

            val englishName = checkNotNull(item.select("div.nameEng")) {
                "Can't find .nameEng element.\n" +
                        "$item"
            }
                .text()
                .takeIf(String::isNotEmpty)

            return KinopoiskExistingMovie(
                url = url,
                russianName = russianName,
                convenientName = englishName ?: russianName,
                releaseYear = releaseYear,
            )
        }
    }
}