package ua.com.radiokot.mcc.base.movie.model

data class ExistingMovieImpl(
    override val convenientName: String,
    override val releaseYear: Int,
) : ExistingMovie