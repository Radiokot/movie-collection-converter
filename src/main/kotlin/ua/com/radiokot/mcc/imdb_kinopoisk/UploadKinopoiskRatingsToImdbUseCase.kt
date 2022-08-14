package ua.com.radiokot.mcc.imdb_kinopoisk

import ua.com.radiokot.mcc.kinopoisk.movie.model.KinopoiskExistingMovie
import ua.com.radiokot.mcc.kinopoisk.rating.KinopoiskRatingsFriendlyCsvSerializer
import ua.com.radiokot.mcc.kinopoisk.rating.model.KinopoiskExistingMovieRating
import java.io.File
import kotlin.math.roundToInt

class UploadKinopoiskRatingsToImdbUseCase(
    private val kinopoiskRatingsCsvFile: File,
    private val imdbKinopoiskMatchedMoviesCsvFile: File,
    private val kinopoiskRatingsFriendlyCsvSerializer: KinopoiskRatingsFriendlyCsvSerializer,
    private val imdbKinopoiskMatchedMoviesFriendlySerializer: ImdbKinopoiskMatchedMoviesFriendlySerializer,
    private val imdbKinopoiskRatingsUploader: ImdbKinopoiskRatingsUploader,
) {
    fun perform() {
        val kinopoiskRatings = kinopoiskRatingsCsvFile
            .inputStream()
            .use { input ->
                kinopoiskRatingsFriendlyCsvSerializer.read(input)
            }
            .sortedBy(KinopoiskExistingMovieRating::date)

        println("Read ${kinopoiskRatings.size} Kinopoisk ratings")

        val minimalMatchesMap = imdbKinopoiskMatchedMoviesCsvFile
            .inputStream()
            .use { input ->
                imdbKinopoiskMatchedMoviesFriendlySerializer.readMinimal(input)
            }

        println("Read ${minimalMatchesMap.size} movie matches")

        val unmatchedMovies = mutableListOf<KinopoiskExistingMovie>()
        val ratingsToUpload = kinopoiskRatings
            .mapNotNull { rating ->
                val imdbTitleId = minimalMatchesMap[rating.movie.url]
                if (imdbTitleId != null) {
                    rating to imdbTitleId
                } else {
                    unmatchedMovies.add(rating.movie)
                    null
                }
            }

        if (unmatchedMovies.isNotEmpty()) {
            println("The following movies are not matched with IMDB and will be skipped:")
            unmatchedMovies.forEach { println("\t$it") }
        }

        val ratingsToUploadCount = ratingsToUpload.size

        println("Upload of $ratingsToUploadCount ratings started")

        var uploadedRatingsCount = 0
        printUploadProgress(uploadedRatingsCount, ratingsToUploadCount)

        ratingsToUpload.chunked(UPLOAD_CHUNK_SIZE).forEach { ratingsChunk ->
            imdbKinopoiskRatingsUploader.upload(ratingsChunk)

            uploadedRatingsCount += ratingsChunk.size

            printUploadProgress(uploadedRatingsCount, ratingsToUploadCount)
        }
    }

    private fun printUploadProgress(
        uploadedRatingsCount: Int,
        ratingsToUploadCount: Int
    ) {
        val uploadingProgress =
            ((uploadedRatingsCount.toDouble() / ratingsToUploadCount) * 100)
                .roundToInt()

        val uploadedProgressBlock = "[${uploadingProgress.toString().padStart(3)}%]"
        println("$uploadedProgressBlock $uploadedRatingsCount of $ratingsToUploadCount ratings uploaded")
    }

    private companion object {
        private const val UPLOAD_CHUNK_SIZE = 10
    }
}