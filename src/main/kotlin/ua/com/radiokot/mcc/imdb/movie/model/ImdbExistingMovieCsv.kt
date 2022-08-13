package ua.com.radiokot.mcc.imdb.movie.model

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonPropertyOrder

@JsonPropertyOrder(
    ImdbExistingMovieCsv.HEADER_ID,
    ImdbExistingMovieCsv.HEADER_CONVENIENT_NAME,
    ImdbExistingMovieCsv.HEADER_RELEASE_YEAR,
    ImdbExistingMovieCsv.HEADER_URL,
)
class ImdbExistingMovieCsv(
    @JsonProperty(HEADER_CONVENIENT_NAME)
    val convenientName: String,

    @JsonProperty(HEADER_RELEASE_YEAR)
    val releaseYear: Int,

    @JsonProperty(HEADER_ID)
    val id: String,

    @JsonProperty(HEADER_URL)
    val url: String,
) {
    constructor(source: ImdbExistingMovie) : this(
        convenientName = source.convenientName,
        releaseYear = source.releaseYear,
        id = source.id,
        url = source.url
    )

    fun toOriginal() = ImdbExistingMovie(
        convenientName = convenientName,
        releaseYear = releaseYear,
        id = id
    )

    companion object {
        const val HEADER_CONVENIENT_NAME = "Convenient name"
        const val HEADER_RELEASE_YEAR = "Release year"
        const val HEADER_URL = "Page"
        const val HEADER_ID = "Title ID"
    }
}