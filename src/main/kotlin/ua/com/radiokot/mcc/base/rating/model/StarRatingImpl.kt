package ua.com.radiokot.mcc.base.rating.model

data class StarRatingImpl(
    override val stars: Float,
    override val of: Int,
) : StarRating {
    init {
        // We don't care about marginal precision when counting stars.
        require(stars >= 0) { "Number of stars must be positive" }
        require(stars <= of) { "Number of stars can't exceed the maximum ($of)" }
    }
}