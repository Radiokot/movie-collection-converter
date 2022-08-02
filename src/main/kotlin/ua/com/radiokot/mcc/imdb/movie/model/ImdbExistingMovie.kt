package ua.com.radiokot.mcc.imdb.movie.model

import ua.com.radiokot.mcc.base.movie.model.ExistingMovie
import ua.com.radiokot.mcc.imdb.api.search.model.ImdbSearchResult

data class ImdbExistingMovie(
    override val convenientName: String,
    override val releaseYear: Int,
    val id: String
) : ExistingMovie {
    val url: String
        get() = "https://www.imdb.com/title/$id/"

    constructor(searchResult: ImdbSearchResult) : this(
        convenientName = searchResult.name,
        releaseYear = searchResult.releaseYear,
        id = searchResult.id
    )
}