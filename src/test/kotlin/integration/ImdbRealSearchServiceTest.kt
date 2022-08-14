package integration

import com.fasterxml.jackson.databind.ObjectMapper
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.junit.Test
import ua.com.radiokot.mcc.imdb.api.search.service.RealImdbSearchService
import ua.com.radiokot.mcc.util.RequestRateLimiter
import ua.com.radiokot.mcc.util.RequestRateLimiterInterceptor

class ImdbRealSearchServiceTest {
    @Test
    fun searchDifferentQueries() {
        val queries = listOf("The Boys", "Paul", "Игла", "Kongen av Bastøy")

        val httpClient = OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .addInterceptor(RequestRateLimiterInterceptor(RequestRateLimiter("IMDB", 100)))
            .build()

        val imdbSearchService =
            RealImdbSearchService(httpClient, ObjectMapper())

        queries.forEach { query ->
            println("$query: ${imdbSearchService.searchTitles(query).firstOrNull()}")
        }
    }
}