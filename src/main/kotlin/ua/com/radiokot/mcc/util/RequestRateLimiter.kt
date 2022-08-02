package ua.com.radiokot.mcc.util

import org.jetbrains.annotations.TestOnly
import java.util.concurrent.CountDownLatch
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

/**
 * Limits external API requests rate by providing timeouts for requestors.
 * Call [waitBeforeRequest] before each API request within the scope.
 * Delayed requests will be queued.
 *
 * @param scope name of the scope the same rate limiter will be used, e.g. VK, IMDB
 * @param requestTimeoutMs number of milliseconds to wait between requests
 */
class RequestRateLimiter(
    private val scope: String,
    private val requestTimeoutMs: Long = 1000L
) {
    private val queue = mutableListOf<CountDownLatch>()
    private val executor = Executors.newSingleThreadScheduledExecutor {
        Thread(it).apply {
            name = "${scope}RequestRateLimiterThread"
            isDaemon = true
        }
    }

    /**
     * Suspends current thread for a time required to safely
     * perform an external API request.
     */
    fun waitBeforeRequest() = synchronized(queue) {
        val currentLatch = queue.lastOrNull() ?: CountDownLatch(0)

        val latchForNext = CountDownLatch(1)
        queue.add(latchForNext)

        val waitFor = requestTimeoutMs * queue.size

        executor.schedule({
            queue.remove(latchForNext)
            latchForNext.countDown()
        }, waitFor, TimeUnit.MILLISECONDS)

        currentLatch.await()
    }

    @TestOnly
    fun reset() {
        queue.clear()
    }
}