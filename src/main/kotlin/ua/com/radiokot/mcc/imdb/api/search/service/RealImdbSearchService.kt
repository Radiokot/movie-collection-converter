package ua.com.radiokot.mcc.imdb.api.search.service

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.treeToValue
import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.OkHttpClient
import okhttp3.Request
import ua.com.radiokot.mcc.imdb.api.search.model.ImdbSearchResult
import ua.com.radiokot.mcc.util.extensions.mapSuccessful
import java.util.*

class RealImdbSearchService(
    private val imdbHttpClient: OkHttpClient,
    private val jsonMapper: ObjectMapper,
) : ImdbSearchService {
    override fun search(query: String): Collection<ImdbSearchResult> {
        val normalizedQuery = query.toLowerCase(Locale.ENGLISH)

        val request = Request.Builder()
            .get()
            .url(
                "https://v3.sg.media-imdb.com/suggestion/_/".toHttpUrl().newBuilder()
                    .addEncodedPathSegment("$normalizedQuery.json")
                    .build()
            )
            .build()

        return imdbHttpClient
            .newCall(request)
            .execute()
            .body
            .let { checkNotNull(it) { "IMDB search response must have a body" } }
            .byteStream()
            .let(jsonMapper::readTree)
            .get("d")
            .mapSuccessful(jsonMapper::treeToValue)
    }
}