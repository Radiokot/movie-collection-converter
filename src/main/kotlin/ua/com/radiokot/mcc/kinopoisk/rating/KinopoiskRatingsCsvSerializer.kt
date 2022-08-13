package ua.com.radiokot.mcc.kinopoisk.rating

import com.fasterxml.jackson.dataformat.csv.CsvMapper
import com.fasterxml.jackson.dataformat.csv.CsvSchema
import ua.com.radiokot.mcc.kinopoisk.rating.model.KinopoiskExistingMovieRating
import ua.com.radiokot.mcc.kinopoisk.rating.model.KinopoiskExistingMovieRatingCsv
import java.io.InputStream
import java.io.OutputStream

class KinopoiskRatingsCsvSerializer(
    private val mapper: CsvMapper,
) {
    private val schema: CsvSchema =
        mapper
            .schemaFor(KinopoiskExistingMovieRatingCsv::class.java)
            .withHeader()

    fun write(
        ratings: Collection<KinopoiskExistingMovieRating>,
        out: OutputStream
    ) {
        mapper
            .writerFor(KinopoiskExistingMovieRatingCsv::class.java)
            .with(schema)
            .writeValues(out)
            .use { writer ->
                writer.writeAll(ratings.map(::KinopoiskExistingMovieRatingCsv))
            }
    }

    fun read(
        input: InputStream
    ): List<KinopoiskExistingMovieRating> {
        return mapper
            .readerFor(KinopoiskExistingMovieRatingCsv::class.java)
            .with(schema)
            .readValues<KinopoiskExistingMovieRatingCsv>(input)
            .use { reader ->
                reader.readAll()
            }
            .map(KinopoiskExistingMovieRatingCsv::toOriginal)
            .toList()
    }
}