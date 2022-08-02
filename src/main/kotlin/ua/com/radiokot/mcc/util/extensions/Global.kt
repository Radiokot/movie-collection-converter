package ua.com.radiokot.mcc.util.extensions

/**
 * @return given [block] result or null if an exception was occurred
 */
inline fun <R : Any> tryOrNull(block: () -> R?) = try {
    block()
} catch (_: Exception) {
    null
}