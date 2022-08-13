package ua.com.radiokot.mcc.kinopoisk.rating.model

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonPropertyOrder
import ua.com.radiokot.mcc.base.rating.model.SimpleStarRating
import ua.com.radiokot.mcc.kinopoisk.movie.model.KinopoiskExistingMovie
import ua.com.radiokot.mcc.kinopoisk.util.KinopoiskDate
import java.time.Instant
import java.time.ZoneId

@JsonPropertyOrder(
    KinopoiskExistingMovieRatingCsv.HEADER_MOVIE_RUSSIAN_NAME,
    KinopoiskExistingMovieRatingCsv.HEADER_MOVIE_RELEASE_YEAR,
    KinopoiskExistingMovieRatingCsv.HEADER_RATING_STARS,
    KinopoiskExistingMovieRatingCsv.HEADER_DATE,
    KinopoiskExistingMovieRatingCsv.HEADER_MOVIE_URL,
    KinopoiskExistingMovieRatingCsv.HEADER_MOVIE_CONVENIENT_NAME,
)
class KinopoiskExistingMovieRatingCsv(
    @JsonProperty(HEADER_MOVIE_CONVENIENT_NAME)
    val movieConvenientName: String,

    @JsonProperty(HEADER_MOVIE_RELEASE_YEAR)
    val movieReleaseYear: Int,

    @JsonProperty(HEADER_MOVIE_RUSSIAN_NAME)
    val movieRussianName: String,

    @JsonProperty(HEADER_MOVIE_URL)
    val movieUrl: String,

    @JsonProperty(HEADER_RATING_STARS)
    val ratingStars: Float,

    @JsonProperty(HEADER_DATE)
    val date: String
) {
    constructor(source: KinopoiskExistingMovieRating) : this(
        movieConvenientName = source.movie.convenientName,
        movieReleaseYear = source.movie.releaseYear,
        movieRussianName = source.movie.russianName,
        movieUrl = source.movie.url,
        ratingStars = source.rating.stars,
        date = KinopoiskDate.formatter
            .withZone(ZoneId.systemDefault())
            .format(source.date)
    )

    fun toOriginal() = KinopoiskExistingMovieRating(
        movie = KinopoiskExistingMovie(
            russianName = movieRussianName,
            url = movieUrl,
            releaseYear = movieReleaseYear,
            convenientName = movieConvenientName
        ),
        rating = SimpleStarRating(
            stars = ratingStars,
            of = KinopoiskExistingMovieRating.MAX_RATING_STARS
        ),
        date = KinopoiskDate.formatter
            .withZone(ZoneId.systemDefault())
            .parse(date, Instant::from)
    )

    companion object {
        const val HEADER_MOVIE_CONVENIENT_NAME = "Movie convenient name"
        const val HEADER_MOVIE_RUSSIAN_NAME = "Movie russian name"
        const val HEADER_MOVIE_RELEASE_YEAR = "Release year"
        const val HEADER_MOVIE_URL = "Movie page"
        const val HEADER_RATING_STARS = "Your rating"
        const val HEADER_DATE = "Rated at (local time)"
    }
}