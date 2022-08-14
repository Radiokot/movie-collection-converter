package ua.com.radiokot.mcc.kinopoisk.rating

import com.fasterxml.jackson.dataformat.csv.CsvMapper
import com.fasterxml.jackson.dataformat.csv.CsvSchema
import ua.com.radiokot.mcc.kinopoisk.rating.model.KinopoiskExistingMovieRating
import ua.com.radiokot.mcc.kinopoisk.rating.model.KinopoiskExistingMovieRatingCsv
import java.io.InputStream
import java.io.OutputStream

class KinopoiskRatingsFriendlyCsvSerializer(
    private val csvMapper: CsvMapper,
    columnSeparator: Char = ';'
) {
    private val schema: CsvSchema =
        csvMapper
            .schemaFor(KinopoiskExistingMovieRatingCsv::class.java)
            .withColumnSeparator(columnSeparator)
            .withHeader()

    fun write(
        ratings: Collection<KinopoiskExistingMovieRating>,
        out: OutputStream
    ) {
        csvMapper
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
        return csvMapper
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