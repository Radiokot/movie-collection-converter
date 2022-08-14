package ua.com.radiokot.mcc.imdb_kinopoisk.model

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonPropertyOrder
import ua.com.radiokot.mcc.base.movie.model.ExistingMovieMatch
import ua.com.radiokot.mcc.imdb.movie.model.ImdbExistingMovie
import ua.com.radiokot.mcc.kinopoisk.movie.model.KinopoiskExistingMovie

@JsonPropertyOrder(
    ImdbKinopoiskMatchedMovieCsv.HEADER_KP_MOVIE_URL,
    ImdbKinopoiskMatchedMovieCsv.HEADER_MATCH_TYPE,
    ImdbKinopoiskMatchedMovieCsv.HEADER_IMDB_MOVIE_ID,
    ImdbKinopoiskMatchedMovieCsv.HEADER_KP_MOVIE_NAME,
    ImdbKinopoiskMatchedMovieCsv.HEADER_IMDB_MOVIE_NAME,
    ImdbKinopoiskMatchedMovieCsv.HEADER_IMDB_MOVIE_URL
)
class ImdbKinopoiskMatchedMovieCsv(
    @JsonProperty(HEADER_KP_MOVIE_URL)
    val kinopoiskMovieUrl: String,

    @JsonProperty(HEADER_MATCH_TYPE)
    val matchType: String,

    @JsonProperty(HEADER_IMDB_MOVIE_ID)
    val imdbMatchedMovieId: String?,

    @JsonProperty(HEADER_KP_MOVIE_NAME)
    val kinopoiskMovieName: String,

    @JsonProperty(HEADER_IMDB_MOVIE_NAME)
    val imdbMatchedMovieName: String?,

    @JsonProperty(HEADER_IMDB_MOVIE_URL)
    val imdbMatchedMovieUrl: String?,
) {
    constructor(
        kinopoiskMovie: KinopoiskExistingMovie,
        match: ExistingMovieMatch<ImdbExistingMovie>
    ) : this(
        kinopoiskMovieUrl = kinopoiskMovie.url,
        matchType = match.type,
        kinopoiskMovieName = kinopoiskMovie.convenientName,
        imdbMatchedMovieName = (match as? ExistingMovieMatch.Found<ImdbExistingMovie>)?.movie?.convenientName,
        imdbMatchedMovieUrl = (match as? ExistingMovieMatch.Found<ImdbExistingMovie>)?.movie?.url,
        imdbMatchedMovieId = (match as? ExistingMovieMatch.Found<ImdbExistingMovie>)?.movie?.id,
    )

    constructor(entry: Map.Entry<KinopoiskExistingMovie, ExistingMovieMatch<ImdbExistingMovie>>) : this(
        kinopoiskMovie = entry.key,
        match = entry.value
    )

    companion object {
        const val HEADER_IMDB_MOVIE_ID = "IMDB title ID [Important]"
        const val HEADER_MATCH_TYPE = "Match type"
        const val HEADER_KP_MOVIE_NAME = "Kinopoisk movie name"
        const val HEADER_KP_MOVIE_URL = "Kinopoisk movie page [Important]"
        const val HEADER_IMDB_MOVIE_NAME = "Matched IMDB movie name"
        const val HEADER_IMDB_MOVIE_URL = "Matched IMDB movie page"
    }
}