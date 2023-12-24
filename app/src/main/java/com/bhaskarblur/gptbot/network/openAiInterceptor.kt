package com.bhaskarblur.gptbot.network

import android.util.Log
import okhttp3.Interceptor
import okhttp3.Response

class OpenAiInterceptor : Interceptor {
    private val headerApiKey = "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"
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val authenticatedRequest = request.newBuilder()
            .addHeader("Authorization", "Bearer $headerApiKey")
            .build()

        Log.d("headers", authenticatedRequest.headers["Authorization"]!!)
        return chain.proceed(authenticatedRequest)
    }
}