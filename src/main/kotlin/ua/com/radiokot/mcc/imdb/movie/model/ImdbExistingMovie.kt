package ua.com.radiokot.mcc.imdb.movie.model

import ua.com.radiokot.mcc.base.movie.model.ExistingMovie

class ImdbExistingMovie(
    override val convenientName: String,
    override val releaseYear: Int,
    val id: String
) : ExistingMovie {
    val url: String
        get() = "https://www.imdb.com/title/$id/"
}