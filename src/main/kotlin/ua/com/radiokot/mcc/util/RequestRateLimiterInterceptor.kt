package ua.com.radiokot.mcc.util

import okhttp3.Interceptor
import okhttp3.Response

class RequestRateLimiterInterceptor(private val rateLimiter: RequestRateLimiter) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        rateLimiter.waitBeforeRequest()
        return chain.proceed(chain.request())
    }
}