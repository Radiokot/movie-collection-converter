package ua.com.radiokot.mcc.base.movie.model

data class SimpleExistingMovie(
    override val convenientName: String,
    override val releaseYear: Int,
) : ExistingMovie