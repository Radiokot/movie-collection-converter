package ua.com.radiokot.mcc.cli

import com.fasterxml.jackson.dataformat.csv.CsvMapper
import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.options.multiple
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.options.required
import com.github.ajalt.clikt.parameters.types.inputStream
import com.github.ajalt.clikt.parameters.types.outputStream
import ua.com.radiokot.mcc.kinopoisk.rating.KinopoiskRatingsFriendlyCsvSerializer
import ua.com.radiokot.mcc.kinopoisk.rating.KinopoiskRatingsHtmlParser
import ua.com.radiokot.mcc.kinopoisk.rating.model.KinopoiskExistingMovieRating
import java.io.InputStream
import java.io.OutputStream
import kotlin.math.roundToInt

class ParseKinopoisk : CliktCommand(
    name = "kinopoisk",
    help = "Parse Kinopoisk ratings collection from HTML file(s) into human-friendly CSV"
) {
    private val inputs: List<InputStream> by option(
        "-i", "--input",
        help = "Input HTML file(s) or stream(s)"
    )
        .inputStream()
        .multiple(required = true)

    private val output: OutputStream by option(
        "-o", "--output",
        help = "Output CSV file or stream"
    )
        .outputStream()
        .required()

    override fun run() {
        val parser = KinopoiskRatingsHtmlParser()

        val parsedRatings = inputs
            .mapIndexed { i, inputStream ->
                parser.parse(inputStream).also {
                    echoPercentageProgress(i + 1, inputs.size) { a, b -> "Parsed $a of $b inputs" }
                }
            }
            .flatten()
            .sortedBy(KinopoiskExistingMovieRating::date)

        echo("Parsed ${parsedRatings.size} ratings")

        KinopoiskRatingsFriendlyCsvSerializer(CsvMapper())
            .write(parsedRatings, output)

        echo("Done")
    }

    fun echoPercentageProgress(
        current: Int,
        total: Int,
        detailedMessage: (current: Int, total: Int) -> String? = { _, _ -> "" }
    ) {
        val percentage = ((current.toDouble() / total) * 100).roundToInt()

        val percentageBlock = "[${percentage.toString().padStart(3)}%]"
        val detailedMessageString = detailedMessage(current, total)

        if (detailedMessageString.isNullOrEmpty()) {
            echo(percentageBlock)
        } else {
            echo("$percentageBlock $detailedMessageString")
        }
    }
}