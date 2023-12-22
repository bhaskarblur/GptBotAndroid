package com.bhaskarblur.gptbot.network

import android.util.Log
import com.bhaskarblur.gptbot.R
import okhttp3.Interceptor
import okhttp3.Response

class OpenAiInterceptor : Interceptor {
    private val headerApiKey = ""
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request();
        val authenticatedRequest = request.newBuilder()
            .addHeader("Authorization", "Bearer ${headerApiKey}")
            .build();

        Log.d("headers",authenticatedRequest.headers.get("Authorization")!!);
        return chain.proceed(authenticatedRequest);
    }
}