package integration

import com.fasterxml.jackson.databind.ObjectMapper
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.junit.Test
import ua.com.radiokot.mcc.imdb.api.search.service.RealImdbSearchService
import ua.com.radiokot.mcc.imdb.movie.matcher.ImdbExistingMoviesMatcher
import ua.com.radiokot.mcc.kinopoisk.rating.parser.KinopoiskRatingsHtmlParser
import ua.com.radiokot.mcc.util.RequestRateLimiter
import java.io.File

class ImdbExistingMoviesMatcherIntegrationTest {
    @Test
    fun matchImdbMoviesForKinopoisk() {
        val kpInput = this.javaClass.getResource("/kp_votes_test.html")!!

        val kpMovies = KinopoiskRatingsHtmlParser()
            .parse(File(kpInput.file))
            .map { it.movie }

        val httpClient = OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .build()

        val imdbSearchService =
            RealImdbSearchService(httpClient, ObjectMapper(), RequestRateLimiter("IMDB", 100))

        val imdbMatcher = ImdbExistingMoviesMatcher(imdbSearchService)

        imdbMatcher.match(kpMovies)
            .forEach(::println)
    }
}