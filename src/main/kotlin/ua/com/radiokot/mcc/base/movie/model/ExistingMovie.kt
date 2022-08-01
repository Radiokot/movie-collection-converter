package ua.com.radiokot.mcc.base.movie.model


/**
 * Minimal data to identify an existing movie.
 */
interface ExistingMovie {
    /**
     * Name of the movie considered convenient, usually
     * an english name.
     */
    val convenientName: String

    /**
     * Gregorian year of the release, e.g. 2014 or 1988.
     */
    val releaseYear: Int
}