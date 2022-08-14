package ua.com.radiokot.mcc.imdb.api.rating.service

import com.fasterxml.jackson.databind.ObjectMapper
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import ua.com.radiokot.mcc.util.RequestRateLimiter

class RealImdbRatingService(
    private val httpClient: OkHttpClient,
    private val jsonMapper: ObjectMapper,
    private val rateLimiter: RequestRateLimiter,
) : ImdbRatingService {

    override fun rateTitle(titleId: String, rating: Int) {
        val bodyString =
            "{\"query\":\"mutation UpdateTitleRating(\$rating: Int!, \$titleId: ID!) " +
                    "{\\n  rateTitle(input: {rating: \$rating, titleId: \$titleId}) " +
                    "{\\n    rating {\\n      value\\n      __typename\\n    }\\n    __typename\\n  }\\n}\",\"operationName\":\"UpdateTitleRating\"," +
                    "\"variables\":{\"rating\":$rating,\"titleId\":\"$titleId\"}}"

        val request = Request.Builder()
            .url("https://api.graphql.imdb.com/")
            .post(bodyString.toRequestBody("application/json".toMediaType()))
            .build()

        rateLimiter.waitBeforeRequest()

        val response = httpClient
            .newCall(request)
            .execute()
            .body

        checkNotNull(response) {
            "Response has no body"
        }

        check(!jsonMapper.readTree(response.byteStream())["data"].isNull) {
            "Response has no data"
        }
    }
}