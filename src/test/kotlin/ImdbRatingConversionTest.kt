import org.junit.Assert
import org.junit.Test
import ua.com.radiokot.mcc.base.rating.model.SimpleStarRating
import ua.com.radiokot.mcc.imdb.rating.model.ImdbExistingMovieRating

class ImdbRatingConversionTest {
    @Test
    fun convertFromTen() {
        val source = SimpleStarRating(
            stars = 8f,
            of = 10
        )
        val converted = ImdbExistingMovieRating.convertRating(source)

        Assert.assertEquals(
            8f,
            converted.stars,
            0.01f
        )
    }

    @Test
    fun convertFromFive() {
        val source = SimpleStarRating(
            stars = 3f,
            of = 5
        )
        val converted = ImdbExistingMovieRating.convertRating(source)

        Assert.assertEquals(
            6f,
            converted.stars,
            0.01f
        )
    }

    @Test
    fun convertFromFiveWithHalves() {
        val source = SimpleStarRating(
            stars = 3.5f,
            of = 5
        )
        val converted = ImdbExistingMovieRating.convertRating(source)

        Assert.assertEquals(
            7f,
            converted.stars,
            0.01f
        )
    }
}