package ua.com.radiokot.mcc.imdb.api.rating.service

class DummyImdbRatingService : ImdbRatingService {
    override fun rateTitle(titleId: String, rating: Int) {
        Thread.sleep(100)
    }
}