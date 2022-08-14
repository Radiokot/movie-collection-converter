package ua.com.radiokot.mcc.imdb.api.rating.service

interface ImdbRatingService {
    /**
     * Applies stars [rating] to the title with ID of [titleId]
     */
    fun rateTitle(
        titleId: String,
        rating: Int,
    )
}