package ua.com.radiokot.mcc.imdb_kinopoisk

import ua.com.radiokot.mcc.imdb.api.rating.service.ImdbRatingService
import ua.com.radiokot.mcc.imdb.rating.model.ImdbExistingMovieRating
import ua.com.radiokot.mcc.kinopoisk.rating.model.KinopoiskExistingMovieRating
import kotlin.math.roundToInt

class ImdbKinopoiskRatingsUploader(
    private val imdbRatingService: ImdbRatingService,
) {
    /**
     * Uploads given [ratings] to IMDB mapping movies using [imdbKinopoiskUrlToIdMap].
     * If no mapping found for a movie, the rating is skipped.
     *
     * @param imdbKinopoiskUrlToIdMap map of Kinopoisk movie URLs associated with IMDB title ID
     */
    fun upload(
        ratings: Collection<KinopoiskExistingMovieRating>,
        imdbKinopoiskUrlToIdMap: Map<String, String>,
    ) {
        ratings.forEach { rating ->
            val imdbTitleId = imdbKinopoiskUrlToIdMap[rating.movie.url]

            if (imdbTitleId != null) {
                imdbRatingService.rateTitle(
                    titleId = imdbTitleId,
                    rating = ImdbExistingMovieRating
                        .convertRating(rating.rating)
                        .stars
                        .roundToInt()
                )
            }
        }
    }
}