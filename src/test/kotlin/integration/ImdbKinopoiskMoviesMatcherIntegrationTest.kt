package integration

import com.fasterxml.jackson.databind.ObjectMapper
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.junit.Test
import ua.com.radiokot.mcc.imdb.api.search.service.RealImdbSearchService
import ua.com.radiokot.mcc.imdb_kinopoisk.ImdbKinopoiskMoviesMatcher
import ua.com.radiokot.mcc.kinopoisk.rating.KinopoiskRatingsHtmlParser
import ua.com.radiokot.mcc.util.RequestRateLimiter
import java.io.File

class ImdbKinopoiskMoviesMatcherIntegrationTest {
    @Test
    fun match() {
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

        val imdbMatcher = ImdbKinopoiskMoviesMatcher(imdbSearchService)

        imdbMatcher.match(kpMovies)
            .forEach(::println)
    }
}