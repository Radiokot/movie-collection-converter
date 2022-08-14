package ua.com.radiokot.mcc.imdb_kinopoisk

import ua.com.radiokot.mcc.imdb.api.rating.service.ImdbRatingService
import ua.com.radiokot.mcc.imdb.rating.model.ImdbExistingMovieRating
import ua.com.radiokot.mcc.kinopoisk.rating.model.KinopoiskExistingMovieRating
import kotlin.math.roundToInt

class ImdbKinopoiskRatingsUploader(
    private val imdbRatingService: ImdbRatingService,
) {
    /**
     * Uploads given ratings to IMDB using matched title IDs.
     *
     * @param ratingsMatchedWithImdbMovieId Kinopoisk movie ratings associated with IMDB title ID
     */
    fun upload(
        ratingsMatchedWithImdbMovieId: Collection<Pair<KinopoiskExistingMovieRating, String>>,
    ) {
        ratingsMatchedWithImdbMovieId.forEach { (rating, imdbTitleId) ->
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