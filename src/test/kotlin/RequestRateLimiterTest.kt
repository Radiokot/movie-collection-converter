import org.junit.Assert
import org.junit.Test
import ua.com.radiokot.mcc.util.RequestRateLimiter
import kotlin.math.roundToInt
import kotlin.system.measureTimeMillis

class RequestRateLimiterTest {
    @Test
    fun singleRequest() {
        val rateLimiter = RequestRateLimiter("test")

        val elapsed = measureTimeMillis { rateLimiter.waitBeforeRequest() }

        Assert.assertTrue(
            "There must be no significant delay for a single first request",
            elapsed <= 50
        )
    }

    @Test
    fun requestsInSeries() {
        val requestTimeoutMs = 1000L
        val rateLimiter = RequestRateLimiter("test", requestTimeoutMs)

        val count = 5
        val elapsed = measureTimeMillis {
            repeat(count) { rateLimiter.waitBeforeRequest() }
        }.toDouble()

        val expectedDelays = count - 1
        Assert.assertEquals(
            "There must be $expectedDelays delays $requestTimeoutMs ms each",
            expectedDelays,
            (elapsed / requestTimeoutMs).roundToInt(),
        )
    }
}