package ua.com.radiokot.mcc.kinopoisk.rating.model

import org.jsoup.nodes.Element
import ua.com.radiokot.mcc.base.rating.model.SimpleStarRating
import ua.com.radiokot.mcc.base.rating.model.StarRating
import ua.com.radiokot.mcc.kinopoisk.movie.model.KinopoiskExistingMovie
import ua.com.radiokot.mcc.kinopoisk.util.KinopoiskDate
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

data class KinopoiskExistingMovieRating(
    val movie: KinopoiskExistingMovie,
    val rating: StarRating,
    val date: Instant
) {
    constructor(
        movie: KinopoiskExistingMovie,
        stars: Float,
        date: Instant
    ) : this(
        movie = movie,
        rating = SimpleStarRating(
            stars = stars,
            of = MAX_RATING_STARS
        ),
        date = date
    )

    companion object {
        const val MAX_RATING_STARS = 10

        /**
         * Creates rating from a list item from a votes page,
         * e.g. https://www.kinopoisk.ru/user/13562461/votes/
         */
        fun fromProfileFilmsListItem(item: Element): KinopoiskExistingMovieRating {
            val dateElement = checkNotNull(item.selectFirst("div.date")) {
                "Can't find .date element.\n" +
                        "$item"
            }

            // Kinopoisk dates are in client's time zone,
            // it's assumed that the capture and the conversion
            // are done on the same computer.
            val date = KinopoiskDate.formatter.parse(dateElement.text(), LocalDateTime::from)
                .atZone(ZoneId.systemDefault())
                .toInstant()

            val stars = checkNotNull(item.select("div.myVote")) {
                "Can't find .myVote element.\n" +
                        "$item"
            }
                .text()
                .toFloat()

            return KinopoiskExistingMovieRating(
                movie = KinopoiskExistingMovie.fromProfileFilmsListItem(item),
                stars = stars,
                date = date
            )
        }
    }
}