import com.fasterxml.jackson.dataformat.csv.CsvMapper
import com.fasterxml.jackson.dataformat.csv.CsvParser
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import org.junit.Assert
import org.junit.Test
import ua.com.radiokot.mcc.base.movie.model.ExistingMovieMatch
import ua.com.radiokot.mcc.imdb.movie.model.ImdbExistingMovie
import ua.com.radiokot.mcc.imdb_kinopoisk.ImdbKinopoiskMatchedMoviesFriendlySerializer
import ua.com.radiokot.mcc.kinopoisk.movie.model.KinopoiskExistingMovie
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream

class ImdbKinopoiskMatchedMoviesFriendlySerializerTest {
    private val mapper = CsvMapper().apply {
        registerKotlinModule()
        enable(CsvParser.Feature.EMPTY_STRING_AS_NULL)
    }

    @Test
    fun write() {
        val matches = mapOf(
            KinopoiskExistingMovie(
                russianName = "kp ru",
                convenientName = "kp conv",
                releaseYear = 2002,
                url = "https://kp1",
            ) to ExistingMovieMatch.Found.Exact(
                ImdbExistingMovie(
                    convenientName = "imdb conv",
                    releaseYear = 2002,
                    id = "imdb1"
                )
            ),

            KinopoiskExistingMovie(
                russianName = "kp ru",
                convenientName = "kp conv",
                releaseYear = 2004,
                url = "https://kp2",
            ) to ExistingMovieMatch.Found.Maybe(
                ImdbExistingMovie(
                    convenientName = "imdb conv",
                    releaseYear = 2004,
                    id = "imdb2"
                )
            ),

            KinopoiskExistingMovie(
                russianName = "kp ru",
                convenientName = "kp conv",
                releaseYear = 2006,
                url = "https://kp3",
            ) to ExistingMovieMatch.NotFound()
        )

        val output = ByteArrayOutputStream()

        ImdbKinopoiskMatchedMoviesFriendlySerializer(mapper)
            .write(matches, output)

        Assert.assertEquals(
            """
                "Kinopoisk movie page [Important]","Match type","IMDB title ID [Important]","Kinopoisk movie name","Matched IMDB movie name","Matched IMDB movie page"
                https://kp3,"Not found",,"kp conv",,
                https://kp2,Maybe,imdb2,"kp conv","imdb conv","https://www.imdb.com/title/imdb2/"
                https://kp1,Exact,imdb1,"kp conv","imdb conv","https://www.imdb.com/title/imdb1/"

            """.trimIndent(),
            output.toByteArray().decodeToString()
        )
    }

    @Test
    fun readCorrectedMinimal() {
        val input = ByteArrayInputStream("""
                "Kinopoisk movie page [Important]","Match type","IMDB title ID [Important]","Kinopoisk movie name","Matched IMDB movie name","Matched IMDB movie page"
                https://kp4,"Not found",,"kp conv",,
                https://kp3,"Not found",imdb3,"kp conv",,
                https://kp2,Maybe,imdb2,"kp conv","imdb conv","https://www.imdb.com/title/imdb2/"
                https://kp1,Exact,imdb1,"kp conv","imdb conv","https://www.imdb.com/title/imdb1/"

            """.trimIndent().toByteArray())

        val minimalMap = ImdbKinopoiskMatchedMoviesFriendlySerializer(mapper)
            .readMinimal(input)

        Assert.assertEquals(
            "imdb1",
            minimalMap["https://kp1"]
        )
        Assert.assertEquals(
            "imdb2",
            minimalMap["https://kp2"]
        )

        Assert.assertEquals(
            "kp3 is manually matched to an ID which must be read",
            "imdb3",
            minimalMap["https://kp3"],
        )
        Assert.assertNull(
            "kp4 is left unmatched and so the read match must be null",
            minimalMap["https://kp4"]
        )
    }
}