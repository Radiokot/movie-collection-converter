package ua.com.radiokot.mcc.base.rating.model

interface StarRating {
    /**
     * Number of stars, positive and not bigger than [of].
     */
    val stars: Float

    /**
     * Max possible number of stars.
     */
    val of: Int
}