package ua.com.radiokot.mcc.imdb.movie.matcher

import ua.com.radiokot.mcc.base.movie.model.ExistingMovie
import ua.com.radiokot.mcc.imdb.api.search.service.ImdbSearchService
import ua.com.radiokot.mcc.imdb.movie.model.ImdbExistingMovie

class ImdbExistingMoviesMatcher(
    private val imdbSearchService: ImdbSearchService
) {

    fun match(movies: Collection<ExistingMovie>): Map<ExistingMovie, ImdbExistingMovie?> {
        TODO()
    }
}