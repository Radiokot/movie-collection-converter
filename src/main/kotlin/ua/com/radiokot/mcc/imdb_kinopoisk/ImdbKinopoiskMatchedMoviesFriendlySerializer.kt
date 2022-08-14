package ua.com.radiokot.mcc.imdb_kinopoisk

import com.fasterxml.jackson.dataformat.csv.CsvMapper
import com.fasterxml.jackson.dataformat.csv.CsvSchema
import ua.com.radiokot.mcc.base.movie.model.ExistingMovieMatch
import ua.com.radiokot.mcc.imdb.movie.model.ImdbExistingMovie
import ua.com.radiokot.mcc.imdb_kinopoisk.model.ImdbKinopoiskMatchedMovieCsv
import ua.com.radiokot.mcc.kinopoisk.movie.model.KinopoiskExistingMovie
import java.io.InputStream
import java.io.OutputStream

/**
 * Human-friendly serializer to manually overview and correct
 * movies matching with a table processor (Excel or Numbers).
 */
class ImdbKinopoiskMatchedMoviesFriendlySerializer(
    private val csvMapper: CsvMapper,
    columnSeparator: Char = ';'
) {
    private val schema: CsvSchema =
        csvMapper
            .schemaFor(ImdbKinopoiskMatchedMovieCsv::class.java)
            .withColumnSeparator(columnSeparator)
            .withHeader()

    // Rows potentially needed manual correction must be on top.
    private val matchTypesOrder = listOf(
        ExistingMovieMatch.NotFound.TYPE,
        ExistingMovieMatch.Found.Maybe.TYPE,
        ExistingMovieMatch.Found.Exact.TYPE
    )

    /**
     * Writes a human-friendly CSV for manual overview and correction.
     */
    fun write(
        matches: Map<KinopoiskExistingMovie, ExistingMovieMatch<ImdbExistingMovie>>,
        out: OutputStream
    ) {
        csvMapper
            .writerFor(ImdbKinopoiskMatchedMovieCsv::class.java)
            .with(schema)
            .writeValues(out)
            .use { writer ->
                val sortedMatches = matches
                    .entries
                    .sortedBy { matchTypesOrder.indexOf(it.value.type) }

                writer.writeAll(sortedMatches.map(::ImdbKinopoiskMatchedMovieCsv))
            }
    }

    /**
     * Reads manually corrected CSV to a map of minimal matches.
     *
     * @return map Kinopoisk movie URL to IMDB title ID regardless match types
     */
    fun readMinimal(
        input: InputStream
    ): Map<String, String?> {
        return csvMapper
            .readerFor(ImdbKinopoiskMatchedMovieCsv::class.java)
            .with(schema)
            .readValues<ImdbKinopoiskMatchedMovieCsv>(input)
            .use { reader ->
                reader.readAll()
            }
            .associate(ImdbKinopoiskMatchedMovieCsv::toMinimalPair)
    }
}